package com.angbot.domain;

import com.angbot.slack.object.SUser;

public class User {
	private String id;

	private String nick;

	private String name;

	private String active;

	public User() {

	}

	public User(SUser user) {
		this.nick = user.getName();
		this.id = user.getId();
		this.name = user.getProfile().getDisplay_name();
		System.out.println("this.name="+this.name);
	}

	public User(User user, String active) {
		this.nick = user.getNick();
		this.id = user.getId();
		this.name = user.getName();
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public String getNick() {
		return nick;
	}

	public String getName() {
		return name;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
