package com.angbot.common;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	public final String JOIN_TYPE = "member_joined_channel";
	public final String PRESENCE_TYPE = "presence_change";
	public static final Logger LOG = LoggerFactory.getLogger(SlackMessageHandler.class);

	ExecutorService executor = Executors.newFixedThreadPool(10);

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(String message, Session userSession) {
		LOG.info("meta data={}", message);
		executor.submit(() -> {
			try {
				ObjectMapper om = new ObjectMapper();
				Map<String, String> result = Maps.newConcurrentMap();
				result = om.readValue(message, Map.class);
				if (result.get("type") != null && result.get("type").equals(JOIN_TYPE)) {
					if(SlackCmdCache.userMap.containsKey(result.get("user"))){
						SlackCmdCache.userMap.get(result.get("user")).setActive("active");
					}else{
						//임시땜빵. 나중에 누가.. 제대로 추가좀.
						((CommCommand) SlackCmdCache.cmdMap.get("!유저")).run(new StringTokenizer(""));
					}
					result.put("type", MSG_TYPE);
					result.put("text", "!사용법");
					userSession.getAsyncRemote().sendText(om.writeValueAsString(result));					
				}
				
				if (result.get("type") != null && result.get("type").equals(PRESENCE_TYPE)) {
					if(SlackCmdCache.userMap.containsKey(result.get("user"))){
						SlackCmdCache.userMap.get(result.get("user")).setActive(result.get("presence"));
					}else{
						//임시땜빵. 나중에 누가.. 제대로 추가좀.
						((CommCommand) SlackCmdCache.cmdMap.get("!유저")).run(new StringTokenizer(""));
					}
				}

				if (result.get("type") != null && result.get("type").equals(MSG_TYPE)) {
					if (result.get("text") != null && result.get("text").startsWith(CMD_TYPE)) {
						String cmd = "";
						StringTokenizer token = new StringTokenizer(result.get("text"), " ");
						if (token.countTokens() > 0) {
							cmd = token.nextToken();
						}
						if (SlackCmdCache.cmdMap.containsKey(cmd)) {
							String resultMsg = "`해당 " + cmd + " 명령은 이미 수행중입니다.`";
							if (!((CommCommand) SlackCmdCache.cmdMap.get(cmd)).isState()) {
								resultMsg = ((CommCommand) SlackCmdCache.cmdMap.get(cmd)).run(token);
							}
							result.put("text", resultMsg);
						} else {
							result.put("text", "`미지원 명령어 입니다.`");
						}
						userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		});

	}

}