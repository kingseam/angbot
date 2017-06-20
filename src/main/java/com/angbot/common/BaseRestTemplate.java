/**
 * 
 */
package com.angbot.common;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BaseRestTemplate {

	private static final Logger LOG = LoggerFactory.getLogger(BaseRestTemplate.class);

	private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/json;charset=utf-8";
	private static final int DEAULT_CONNECTION_TIME_OUT = 10000;
	private static final int DEAULT_REQUEST_TIME_OUT = 10000;

	private RestTemplate restTemplate;
	private HttpComponentsClientHttpRequestFactory requestFactory;
	private HttpHeaders headers;

	public BaseRestTemplate() {
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
		requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectionRequestTimeout(DEAULT_REQUEST_TIME_OUT);
		requestFactory.setConnectTimeout(DEAULT_CONNECTION_TIME_OUT);
	}

	public HttpHeaders getHttpHeaders() {
		return headers;
	}

	public void setHeader() {
		headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, DEFAULT_CONTENT_TYPE_VALUE);
	}

	public <T extends Object> T getApiCaller(String url, Class<T> callback, Map<String, Object> param) {

		LOG.info("Request >> " + url);
		LOG.info("param >> " + param);
		if (getHttpHeaders() == null) {
			// throw new
			// BizMsgException(RecipeAPICode.EMPTY_HTTP_HEADER.getCode());
		}

		HttpEntity<String> entity = new HttpEntity<String>(getHttpHeaders());

		long start = System.currentTimeMillis();

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, param);

		LOG.info("response  >>{} ", response);

		long end = System.currentTimeMillis();
		LOG.info("RequestTime  >> " + (end - start) / 1000);

		LOG.info("StatusCode >> " + response.getStatusCode());
		// TODO 추후 별도 API Log Message 저장필요.
		// LOG.info("Response >> " + response.getBody() );

		return responseHandler(response, callback);
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
