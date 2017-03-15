
package com.angbot.common;

import org.springframework.http.ResponseEntity;

public interface IResponseHandler {

	public <T extends Object> T responseToObject(ResponseEntity<String> response, Class<T> callback) throws Exception;

}
