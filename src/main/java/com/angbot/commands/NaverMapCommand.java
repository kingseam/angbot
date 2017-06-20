package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.SlackCommService;

public class NaverMapCommand extends CommCommand{
	
	public NaverMapCommand(SlackCommService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!어디야";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		// TODO Auto-generated method stub
		return "`네이버 검석해 그냥..?`";
	}
}
