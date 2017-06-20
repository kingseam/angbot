package com.angbot.common;

import java.util.Map;
import java.util.StringTokenizer;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angbot.commands.CommCommand;
import com.angbot.service.SlackCmdCache;
import com.google.common.collect.Maps;

public class SlackMessageHandler implements MessageHandler {
	public final String CMD_TYPE = "!";
	public final String MSG_TYPE = "message";
	public static final Logger LOG = LoggerFactory.getLogger(SlackMessageHandler.class);

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(String message, Session userSession) {
		LOG.info("meta data={}",message);
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
					}else{
						result.put("text", "`미지원 명령어 입니다.`");						
					}
					userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
				}
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}

}