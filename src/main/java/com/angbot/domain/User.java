package com.angbot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	
	public User() {
	
	}

	public User(SUser user) {		
		this.nick = user.getName();
		this.id  = user.getId();
		this.name = user.getReal_name();
	}
}
