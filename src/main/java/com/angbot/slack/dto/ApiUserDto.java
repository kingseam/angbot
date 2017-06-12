package com.angbot.slack.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.angbot.slack.object.SUser;

public class ApiUserDto extends ApiBaseDto {
	@JsonProperty("ok")
	private boolean result;

	@JsonProperty("members")
	private List<SUser> responseItem;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<SUser> getResponseItem() {
		return responseItem;
	}

	public void setResponseItem(List<SUser> responseItem) {
		this.responseItem = responseItem;
	}

}
