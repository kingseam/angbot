package com.angbot.slack.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;

public class SUser {

	private String id;
	private String team_id;
	private String name;
	private String deleted;
	private String status;
	private String color;
	private String real_name;
	private String tz;
	private String tz_label;
	private String tz_offset;
	private String is_admin;
	private String is_owner;
	private String is_primary_owner;
	private String is_restricted;
	private String is_ultra_resricted;
	private String has_2fa;
	private String two_factor_type;
	private Profile profile;

	public SUser() {

	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getTz() {
		return tz;
	}

	public void setTz(String tz) {
		this.tz = tz;
	}

	public String getTz_label() {
		return tz_label;
	}

	public void setTz_label(String tz_label) {
		this.tz_label = tz_label;
	}

	public String getTz_offset() {
		return tz_offset;
	}

	public void setTz_offset(String tz_offset) {
		this.tz_offset = tz_offset;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

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

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(String is_admin) {
		this.is_admin = is_admin;
	}

	public String getIs_owner() {
		return is_owner;
	}

	public void setIs_owner(String is_owner) {
		this.is_owner = is_owner;
	}

	public String getIs_primary_owner() {
		return is_primary_owner;
	}

	public void setIs_primary_owner(String is_primary_owner) {
		this.is_primary_owner = is_primary_owner;
	}

	public String getIs_restricted() {
		return is_restricted;
	}

	public void setIs_restricted(String is_restricted) {
		this.is_restricted = is_restricted;
	}

	public String getIs_ultra_resricted() {
		return is_ultra_resricted;
	}

	public void setIs_ultra_resricted(String is_ultra_resricted) {
		this.is_ultra_resricted = is_ultra_resricted;
	}

	public String getHas_2fa() {
		return has_2fa;
	}

	public void setHas_2fa(String has_2fa) {
		this.has_2fa = has_2fa;
	}

	public String getTwo_factor_type() {
		return two_factor_type;
	}

	public void setTwo_factor_type(String two_factor_type) {
		this.two_factor_type = two_factor_type;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
