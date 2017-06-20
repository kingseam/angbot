package com.angbot.slack.object;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Channel {
	private String id;
	private String name;
	private String created;
	private String is_archived;
	private String is_member;
	private String num_members;
	private Topic topic;
	private Topic purpose;
	private String subject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getIs_archived() {
		return is_archived;
	}

	public void setIs_archived(String is_archived) {
		this.is_archived = is_archived;
	}

	public String getIs_member() {
		return is_member;
	}

	public void setIs_member(String is_member) {
		this.is_member = is_member;
	}

	public String getNum_members() {
		return num_members;
	}

	public void setNum_members(String num_members) {
		this.num_members = num_members;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Topic getPurpose() {
		return purpose;
	}

	public void setPurpose(Topic purpose) {
		this.purpose = purpose;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
