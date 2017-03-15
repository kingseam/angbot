/**
 * 
 */
package com.angbot.common;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JsonResponseHandler implements IResponseHandler {

	@Override
	public <T> T responseToObject(ResponseEntity<String> response, Class<T> callback) throws Exception {

		ObjectMapper om = new ObjectMapper();
		
		om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		System.out.println("aaaa="+response.getBody());
		T resultObject = om.readValue(response.getBody(), callback);
		
		System.out.println("aaaa="+resultObject);

		return resultObject;

	}

}
