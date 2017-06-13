package com.angbot.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@SuppressWarnings("serial")
public class ApiResDto{
	private String method = null;
	private String res_code = null;
	private String res_message = null;
	private Map<String,Object> data;

	public ApiResDto() {
		this("");
	}

	public ApiResDto(String method) {
		this.method = method;
		this.res_code = "200";
		this.res_message = "OK";
		this.data = new HashMap<String,Object>();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRes_code() {
		return res_code;
	}

	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}

	public String getRes_message() {
		return res_message;
	}

	public void setRes_message(String res_message) {
		this.res_message = res_message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public Map<String,Object> putData(String key, Object value) {
	    data.put(key, value);
	    return data;
	}

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
