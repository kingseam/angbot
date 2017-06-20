package com.angbot.util;

public enum CodeCommand {
	CM_USER("!유저", "SlackUserCommand"),
	CM_MAP("!어디야", "NaverMapCommand"), 
	CM_TEST("!초보", "CommCommand")

	;
	private final String baseContext = "com.angbot.commands.";
	private String message;
	private String code;

	private CodeCommand(String message, String code) {
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public String getContext() {
		return baseContext+code;
	}

}
