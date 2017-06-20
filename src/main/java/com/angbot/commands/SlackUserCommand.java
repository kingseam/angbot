package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.SlackCommService;

public class SlackUserCommand extends CommCommand{
	
	public SlackUserCommand(SlackCommService service) {
		super(service);
	}
	@Override
	public String command() {
		return "!유저";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		// TODO Auto-generated method stub
		return "`ok?`";
	}
}
