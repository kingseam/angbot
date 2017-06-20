package com.angbot.slack.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import com.angbot.slack.object.Self;
import com.angbot.slack.object.Team;

public class ApiRealTimeMessageDto extends ApiBaseDto {

	@JsonProperty("url")
	private String url;
	@JsonProperty("team")
	private Team team;
	@JsonProperty("self")
	private Self self;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

}
