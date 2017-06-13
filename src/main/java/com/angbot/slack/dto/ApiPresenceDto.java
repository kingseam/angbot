package com.angbot.slack.dto;

public class ApiPresenceDto extends ApiBaseDto {
	private boolean ok;
	private String presence;
	private String online;
	private String auto_away;
	private String manual_away;
	private String connection_count;
	private String last_activity;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getPresence() {
		return presence;
	}

	public void setPresence(String presence) {
		this.presence = presence;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getAuto_away() {
		return auto_away;
	}

	public void setAuto_away(String auto_away) {
		this.auto_away = auto_away;
	}

	public String getManual_away() {
		return manual_away;
	}

	public void setManual_away(String manual_away) {
		this.manual_away = manual_away;
	}

	public String getConnection_count() {
		return connection_count;
	}

	public void setConnection_count(String connection_count) {
		this.connection_count = connection_count;
	}

	public String getLast_activity() {
		return last_activity;
	}

	public void setLast_activity(String last_activity) {
		this.last_activity = last_activity;
	}

}
