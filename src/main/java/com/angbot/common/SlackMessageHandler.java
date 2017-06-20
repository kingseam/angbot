package com.angbot.common;

import java.util.Map;
import java.util.StringTokenizer;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;

import com.angbot.commands.CommCommand;
import com.angbot.service.SlackCmdCache;
import com.angbot.service.SlackCommService;
import com.google.common.collect.Maps;

public class SlackMessageHandler implements MessageHandler {
	public final String CMD_TYPE = "!";
	public final String MSG_TYPE = "message";

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(String message, Session userSession) {
		try{
			ObjectMapper om = new ObjectMapper();
			Map<String, String> result = Maps.newConcurrentMap();
			result = om.readValue(message, Map.class);
			if (result.get("type") != null && result.get("type").equals(MSG_TYPE)){
				if (result.get("text") != null && result.get("text").startsWith(CMD_TYPE)) {
					StringTokenizer token = new StringTokenizer(result.get("text"), " ");
					String cmd = "";
					if(token.countTokens() > 0){
						cmd = token.nextToken();
					}
					
					if(SlackCmdCache.cmdMap.containsKey(cmd)){
						String resultMsg = ((CommCommand)SlackCmdCache.cmdMap.get(cmd)).run(token);
						result.put("text", resultMsg);
						userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
					}
					
				}
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}

}