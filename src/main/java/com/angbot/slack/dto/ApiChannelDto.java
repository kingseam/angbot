package com.angbot.slack.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.angbot.slack.object.Channel;

public class ApiChannelDto extends ApiBaseDto {
	@JsonProperty("channels")
	private List<Channel> responseItem;

	public List<Channel> getResponseItem() {
		return responseItem;
	}

	public void setResponseItem(List<Channel> responseItem) {
		this.responseItem = responseItem;
	}

}
