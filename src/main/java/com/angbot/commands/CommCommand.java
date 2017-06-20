package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public abstract class CommCommand {
	CommandApiService service;
	
	public CommCommand(CommandApiService service) {
		this.service = service;
	}
	
	public abstract String command();

	public abstract String run(StringTokenizer token) throws Exception;
}
