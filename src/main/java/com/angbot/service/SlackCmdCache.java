package com.angbot.service;

import java.util.HashMap;

import java.util.Map;

import com.angbot.commands.HelpCommand;
import com.angbot.commands.NaverMapCommand;
import com.angbot.commands.SlackChannelCommand;
import com.angbot.commands.SlackUserCommand;

public class SlackCmdCache {
	public static Map<String, Object> cmdMap = new HashMap<String, Object>();

	public static final Class[] registerdCommands = { 
			NaverMapCommand.class, 
			SlackUserCommand.class,
			SlackChannelCommand.class,
			HelpCommand.class
    };
	
	public SlackCmdCache() {
		// TODO Auto-generated constructor stub
	}
}