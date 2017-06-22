/**
 * 
 */
package com.angbot.common;

import java.util.Map;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverRestTemplate {

	private static final Logger LOG = LoggerFactory.getLogger(NaverRestTemplate.class);

	private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/json;charset=utf-8";
	private static final String NAVER_API_ID = "X-Naver-Client-Id";
	private static final String NAVER_API_SECRET = "X-Naver-Client-Secret";

	private static final int DEAULT_CONNECTION_TIME_OUT = 12000;
	private static final int DEAULT_REQUEST_TIME_OUT = 10000;

	private RestTemplate restTemplate;
	private HttpComponentsClientHttpRequestFactory requestFactory;
	private HttpHeaders headers;

	@Value("${naver.api.id}")
	private String api_id;

	@Value("${naver.api.secret}")
	private String api_secret;

	public NaverRestTemplate() {
		try {
			restTemplate = new RestTemplate();
			setClientFactory();
			setHeader();
			restTemplate.setRequestFactory(requestFactory);
		} catch (Exception e) {
			LOG.error("SlackRestTemplate init error = {}", e);
		}

	}

	public void setClientFactory() {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		requestFactory.setConnectionRequestTimeout(DEAULT_REQUEST_TIME_OUT);
		requestFactory.setConnectTimeout(DEAULT_CONNECTION_TIME_OUT);
	}

	public ClientHttpRequestFactory getClientFactory() {
		return requestFactory;
	}

	public HttpHeaders getHttpHeaders() {
		setHeader();
		return headers;
	}

	public void setHeader() {
		headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, DEFAULT_CONTENT_TYPE_VALUE);
		headers.add(NAVER_API_ID, api_id);
		headers.add(NAVER_API_SECRET, api_secret);
	}

	public String getApiCaller(String url, Map<String, Object> param) {
		LOG.info("Request >> " + url);
		LOG.info("param >> " + param);
		if (getHttpHeaders() == null) {
			// throw new
			// BizMsgException(RecipeAPICode.EMPTY_HTTP_HEADER.getCode());
		}

		HttpEntity<String> entity = new HttpEntity<String>(getHttpHeaders());

		long start = System.currentTimeMillis();

		LOG.info("response  >>{} ", entity);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, param);

		LOG.info("response  >>{} ", response);

		long end = System.currentTimeMillis();
		LOG.info("RequestTime  >> " + (end - start) / 1000);

		LOG.info("StatusCode >> " + response.getStatusCode());
		// TODO 추후 별도 API Log Message 저장필요.
		// LOG.info("Response >> " + response.getBody() );

		return response.getBody();
	}
	
	public String postApiCaller(String url, Map<String, Object> param) {
		LOG.info("Request >> " + url);
		LOG.info("param >> " + param);
		if (getHttpHeaders() == null) {
			// throw new
			// BizMsgException(RecipeAPICode.EMPTY_HTTP_HEADER.getCode());
		}

		HttpEntity<String> entity = new HttpEntity<String>(getHttpHeaders());

		long start = System.currentTimeMillis();

		LOG.info("response  >>{} ", entity);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, param);

		LOG.info("response  >>{} ", response);

		long end = System.currentTimeMillis();
		LOG.info("RequestTime  >> " + (end - start) / 1000);

		LOG.info("StatusCode >> " + response.getStatusCode());
		// TODO 추후 별도 API Log Message 저장필요.
		// LOG.info("Response >> " + response.getBody() );

		return response.getBody();
	}

	public <T> T responseHandler(ResponseEntity<String> response, Class<T> callback) {
		IResponseHandler responseHandler = new JsonResponseHandler();
		T resultObject = null;

		if (HttpStatus.OK == response.getStatusCode()) {
			try {
				resultObject = responseHandler.responseToObject(response, callback);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// TODO throw new BizMsgException(errorMessage.getMessage());
		}

		return resultObject;
	}
}
