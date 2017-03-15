package com.angbot.util;

public enum CodeS {
	GET_USERS("/api/users.list?token={token}&pretty={pretty}"),
	
	S_E001("Slack Api 연동 실패", "001"),
	
	;

	private String url;
	private String message;
	private String code;
	
	private final String BASE_URL = "https://slack.com";

	private CodeS(String url) {
		this.url = url;
	}
	
	private CodeS(String message, String code) {		
		this.message = message;
		this.code = code;
	}

	public String getUrl() {

		return BASE_URL.concat(url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBASE_URL() {
		return BASE_URL;
	}
	
	
}
