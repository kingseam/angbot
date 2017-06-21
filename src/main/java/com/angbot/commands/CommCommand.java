package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public abstract class CommCommand {
	public CommandApiService service;
	
	public CommCommand(CommandApiService service) {
		this.service = service;
	}
	
	public abstract String command();
	
	public abstract String help();

	public abstract String run(StringTokenizer token) throws Exception;

	public abstract boolean isState();
}
