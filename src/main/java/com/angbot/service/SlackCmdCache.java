package com.angbot.service;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import com.angbot.commands.GitHubIssueCommand;
import com.angbot.commands.HelpCommand;
import com.angbot.commands.NLPCommand;
import com.angbot.commands.NaverBlogCommand;
import com.angbot.commands.NaverCafeCommand;
import com.angbot.commands.NaverDocumentCommand;
import com.angbot.commands.NaverImageCommand;
import com.angbot.commands.NaverMapCommand;
import com.angbot.commands.NoticeCommand;
import com.angbot.commands.RecipeCommand;
import com.angbot.commands.SlackChannelCommand;
import com.angbot.commands.SlackUserCommand;
import com.angbot.commands.WeatherCommand;
import com.angbot.domain.User;

public class SlackCmdCache {
	public static Map<String, Object> cmdMap = new LinkedHashMap<String, Object>();
	public static Map<String, User> userMap = new LinkedHashMap<String, User>();

	public static final Class<?>[] registerdCommands = {
			NoticeCommand.class,
			SlackChannelCommand.class,
			SlackUserCommand.class,
			NaverMapCommand.class,
			HelpCommand.class,
			NaverBlogCommand.class,
			NaverImageCommand.class,
			NaverDocumentCommand.class,
			NaverCafeCommand.class,
			WeatherCommand.class,
			GitHubIssueCommand.class,
			NLPCommand.class,
			RecipeCommand.class
    };

	public static Word2Vec vec;

	public SlackCmdCache() {

	}

	public static void initWord2Vec(){
		 try {
				vec = WordVectorSerializer.loadFullModel("/home/hosting_users/angbot/out_20170926_1448.out");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}