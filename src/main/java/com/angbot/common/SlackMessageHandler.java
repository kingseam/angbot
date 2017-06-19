package com.angbot.common;

import java.lang.reflect.Method;
import java.util.Map;
import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import com.angbot.util.CodeCommand;
import com.google.common.collect.Maps;

public class SlackMessageHandler implements MessageHandler {
	ClassLoader loader;
	Class<?> clazz;
	Object instance;
	Method method;

	public SlackMessageHandler() throws Exception {
	}

	@Override
	public void handleMessage(String message, Session userSession) {
		try{
			ObjectMapper om = new ObjectMapper();
			Map<String, String> result = Maps.newConcurrentMap();
			result = om.readValue(message, Map.class);
			if (result.get("type") != null && result.get("type").equals("message")) {
				if (result.get("text") != null && result.get("text").startsWith("!")) {
					Map<String, String> msg = Maps.newConcurrentMap();
					msg.put("type", "message");
					msg.put("channel", result.get("channel"));
					msg.put("text", (String) resultExcute(result));
	
					userSession.getAsyncRemote().sendText(om.writeValueAsString(msg));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("finally")
	public String resultExcute(Map<String, String> command){
		//TODO 변수명 및 .. 구조 한번 다시 생각해봐야됨.
		String[] token = command.get("text").split(" ");
		String cmd = "";
		String func = "";
		String context = "";
		String param = "";
		String result = "";
		if (token.length > 0) {
			cmd = token[0];
		}

		try{
			if (cmd.equals(CodeCommand.CM_MAP.getMessage())) {
				context = CodeCommand.CM_MAP.getContext();
				loader = Thread.currentThread().getContextClassLoader();
				clazz = loader.loadClass(context);
				instance = clazz.newInstance();
				
				if(token.length > 1){
					func = "excute";
					param = token[1];
				}else{
					func = "printHelp";
				}
				
				method = clazz.getMethod(func,  new Class[] {String.class});
				
				result = (String) method.invoke(instance, param);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return result;
		}
		
	}
}