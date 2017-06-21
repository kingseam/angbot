package com.angbot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.angbot.slack.object.SUser;

@Entity
public class User {
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nick")
	private String nick;

	@Column(name = "name")
	private String name;

	@Column(name = "active")
	private String active;

	public User() {

	}

	public User(SUser user) {
		this.nick = user.getName();
		this.id = user.getId();
		this.name = user.getReal_name();
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
