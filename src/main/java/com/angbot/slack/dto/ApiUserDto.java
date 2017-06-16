package com.angbot.slack.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.angbot.slack.object.SUser;

public class ApiUserDto extends ApiBaseDto {

	@JsonProperty("members")
	private List<SUser> responseItem;

	public List<SUser> getResponseItem() {
		return responseItem;
	}

	public void setResponseItem(List<SUser> responseItem) {
		this.responseItem = responseItem;
	}

}
