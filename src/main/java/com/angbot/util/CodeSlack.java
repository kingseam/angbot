package com.angbot.util;

public enum CodeSlack {
	GET_USERS("/api/users.list?token={token}&pretty={pretty}"),
	GET_Active("/api/users.getPresence?token={token}&user={user}"),
	GET_CHANEELS("/api/channels.list?token={token}&exclude_members=false&exclude_archived=false"),
	GET_RTMSTART("/api/rtm.connect?token={token}&pretty={pretty}"),
	
	S_E001("Slack Api 연동 실패", "001"),
	
	;

	private String url;
	private String message;
	private String code;
	
	private final String BASE_URL = "https://slack.com";

	private CodeSlack(String url) {
		this.url = url;
	}
	
	private CodeSlack(String message, String code) {		
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



