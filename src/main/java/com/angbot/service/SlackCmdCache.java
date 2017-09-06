package com.angbot.service;

import java.util.HashMap;
import java.util.Map;

import com.angbot.commands.HelpCommand;
import com.angbot.commands.NaverBlogCommand;
import com.angbot.commands.NaverCafeCommand;
import com.angbot.commands.NaverDocumentCommand;
import com.angbot.commands.NaverImageCommand;
import com.angbot.commands.NaverMapCommand;
import com.angbot.commands.SlackChannelCommand;
import com.angbot.commands.SlackUserCommand;
import com.angbot.commands.WeatherCommand;

public class SlackCmdCache {
	public static Map<String, Object> cmdMap = new HashMap<String, Object>();

	public static final Class[] registerdCommands = { 
			NaverMapCommand.class, 
			SlackUserCommand.class,
			SlackChannelCommand.class,
			HelpCommand.class,
			NaverBlogCommand.class,
			NaverImageCommand.class,
			NaverDocumentCommand.class,
			NaverCafeCommand.class,
			WeatherCommand.class
    };
	
	public SlackCmdCache() {
		// TODO Auto-generated constructor stub
	}
}