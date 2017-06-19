package com.angbot.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


@ClientEndpoint
public class WebsocketClientEndpoint {
    public MessageHandler messageHandler;
    public Session userSession;
    public WebSocketContainer container;
    public String url = "";
    
    private WebsocketClientEndpoint(WebsocketClientBuilder builder) throws DeploymentException, IOException, URISyntaxException  {
    	this.url = builder.url;
    	this.messageHandler = builder.messageHandler;
    	container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, new URI(url));
	}
    
	public static class WebsocketClientBuilder{
	    public String url = "";
	    public MessageHandler messageHandler;
	    
	    public WebsocketClientBuilder() {
		};
	    
	    public WebsocketClientBuilder setURI(String url){
	    	this.url = url;	    	
	    	return this;
	    }
	    
	    public WebsocketClientBuilder setServer(MessageHandler messageHandler){
    		this.messageHandler = messageHandler;
	    	return this;
	    }
	    
		public WebsocketClientEndpoint build() throws DeploymentException, IOException, URISyntaxException{
			return new WebsocketClientEndpoint(this);
		}
	}
    
    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message, userSession);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
}