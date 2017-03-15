package com.angbot.util;

public enum CodeS {
	GET_USERS("/api/users.list?token={token}&pretty={pretty}"),;

	private String url;
	private final String BASE_URL = "https://slack.com";

	private CodeS(String url) {
		this.url = url;
	}

	public String getUrl() {

		return BASE_URL.concat(url);
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
