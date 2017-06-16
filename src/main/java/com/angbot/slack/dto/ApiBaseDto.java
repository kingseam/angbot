package com.angbot.slack.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class ApiBaseDto {
	@JsonProperty("ok")
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
