package com.angbot.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.angbot.commands.GitHubIssueCommand;
import com.angbot.commands.HelpCommand;
import com.angbot.commands.NLPCommand;
import com.angbot.commands.NaverBlogCommand;
import com.angbot.commands.NaverCafeCommand;
import com.angbot.commands.NaverDocumentCommand;
import com.angbot.commands.NaverImageCommand;
import com.angbot.commands.NaverMapCommand;
import com.angbot.commands.NoticeCommand;
import com.angbot.commands.SlackChannelCommand;
import com.angbot.commands.SlackUserCommand;
import com.angbot.domain.User;

public class SlackCmdCache {
	public static Map<String, Object> cmdMap = new LinkedHashMap<String, Object>();
	public static Map<String, User> userMap = new LinkedHashMap<String, User>();

	public static final Class[] registerdCommands = {
			NoticeCommand.class,
			SlackChannelCommand.class,
			SlackUserCommand.class,
			NaverMapCommand.class,
			HelpCommand.class,
			NaverBlogCommand.class,
			NaverImageCommand.class,
			NaverDocumentCommand.class,
			NaverCafeCommand.class,
			GitHubIssueCommand.class,
			NLPCommand.class
    };

	public SlackCmdCache() {
		// TODO Auto-generated constructor stub
	}
}