package com.angbot.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import org.openkoreantext.processor.util.KoreanPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angbot.commands.CommCommand;
import com.angbot.domain.User;
import com.angbot.service.SlackCmdCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import scala.collection.JavaConverters;
import scala.collection.Seq;

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
				// 제너럴일경우만. 사용법
				if (result.get("type") != null && result.get("type").equals(JOIN_TYPE) && result.get("channel").equals("C2F31LCTZ")) {
					if(SlackCmdCache.userMap.containsKey(result.get("user"))){
						SlackCmdCache.userMap.get(result.get("user")).setActive("active");
					}else{
						//임시땜빵. 나중에 누가.. 제대로 추가좀.
						//((CommCommand) SlackCmdCache.cmdMap.get("!유저 동기화")).run(new StringTokenizer(""));
					}
					result.put("type", MSG_TYPE);
					result.put("text", "!사용법");
					userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
				}

				if (result.get("type") != null && result.get("type").equals(PRESENCE_TYPE)) {
					if(SlackCmdCache.userMap.get(result.get("user")) != null){
						SlackCmdCache.userMap.get(result.get("user")).setActive(result.get("presence"));
					}else{
						//임시땜빵. 나중에 누가.. 제대로 추가좀.
						//((CommCommand) SlackCmdCache.cmdMap.get("!유저 동기화")).run(new StringTokenizer(""));
						System.out.println("누구냐?" + result.get("user"));
					}
				}

				if (result.get("type") != null && result.get("type").equals(MSG_TYPE)) {
					if (result.get("text") != null && result.get("text").startsWith(CMD_TYPE)) {
						String cmd = "";
						StringTokenizer token = new StringTokenizer(result.get("text"), " ");
						if (token.countTokens() > 0) {
							cmd = token.nextToken();
						}

						if("!유저".equals(cmd)){
							List<String> _userList = Lists.newArrayList();
							for(Entry<String, User> entry : SlackCmdCache.userMap.entrySet()){
								_userList.add(entry.getValue().getId());
							}
							Map<String, Object> _temp = Maps.newConcurrentMap();
							_temp.put("type", "presence_query");
							_temp.put("ids", _userList);
							Gson gson = new Gson();
							String json = gson.toJson(_temp);
							System.out.println(json);
							userSession.getAsyncRemote().sendText(json);
							Thread.sleep(1500);
							result.put("text",((CommCommand) SlackCmdCache.cmdMap.get(cmd)).run(token));
						}else if (SlackCmdCache.cmdMap.containsKey(cmd)) {
							String resultMsg = "`해당 " + cmd + " 명령은 이미 수행중입니다.`";
							if (!((CommCommand) SlackCmdCache.cmdMap.get(cmd)).isState()) {
								resultMsg = ((CommCommand) SlackCmdCache.cmdMap.get(cmd)).run(token);
							}
							result.put("text", resultMsg);

						} else {
							result.put("text", "`미지원 명령어 입니다.`");
						}
						userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
					}else if(result.get("text") != null){
					//}else if(result.get("text") != null && SlackCmdCache.userMap.get(result.get("user")).getNick().equals("angmagun")){
						String[] tokens = result.get("text").split(" ");
						for(String temp : tokens){
							List<String> dvec = Wordvec.vec.similarWordsInVocabTo(temp,0.7);
							System.out.println("temp=" + temp);
							List<String> resultList = new ArrayList<>();
							for(String tem : dvec){
								System.out.println("tem=" + tem);
								resultList.add(tem);
							}
							if(resultList.size() > 0) {
								result.put("text", "`" + SlackCmdCache.userMap.get(result.get("user")).getNick() + "문장에서 쓰레기 유사단어 감지(" + String.join(",", resultList) + ") 너님 쓰레기아님`");
								userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
							}

							//System.out.println("dvec="+dvec);
							//result.put("text", "`" + SlackCmdCache.userMap.get(result.get("user")).getNick() + temp +" 아님.`");
							//userSession.getAsyncRemote().sendText(om.writeValueAsString(result));
						}
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		});

	}

}
