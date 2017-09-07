package com.angbot.util;

public enum CodeNaver {
	GET_MAP("/v1/search/local.json?sort=random&display=1&query={query}"),
	GET_BLOG("/v1/search/blog.json?display=1&sort=sim&query={query}"),
	GET_IMAGE("/v1/search/image.json?display=1&sort=sim&filter=medium&query={query}"),
	GET_DOCUMENT("/v1/search/encyc.json?display=1&query={query}"),
	GET_CAFE("/v1/search/cafearticle.json?display=5&sort=date&query=javachobostudy"),
	GET_SHORTURL("/v1/util/shorturl?url={url}"),
	
	S_E001("Naver Api 연동 실패", "001"),
	
	;

	private String url;
	private String message;
	private String code;
	
	private final String BASE_URL = "https://openapi.naver.com";

	private CodeNaver(String url) {
		this.url = url;
	}
	
	private CodeNaver(String message, String code) {		
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