package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.SlackCommService;

public abstract class CommCommand {
	SlackCommService service;
	
	public CommCommand(SlackCommService service) {
		this.service = service;
	}
	
	public abstract String command();

	public abstract String run(StringTokenizer token) throws Exception;
}
