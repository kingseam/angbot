package com.angbot.common;

import javax.websocket.Session;

public interface MessageHandler {
	public void handleMessage(String message, Session userSession);
}