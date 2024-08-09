package com.worksuite.websocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
 
@ServerEndpoint("/websocket")
public class MyWebSocket{
	
	private static Set<Session> setOfChatRooms = Collections.synchronizedSet(new HashSet<Session>());
	@OnOpen
    public void onOpen(Session session){
        System.out.println("Open Connection ..." + session.getId());
        setOfChatRooms.add(session);
    }
     
    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }
     
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        
        // Send the message to all connected clients
        for (Session client : session.getOpenSessions()) {
        	if(session.getId() != client.getId()) {
        		client.getBasicRemote().sendText(echoMsg + " from Id " + client.getId());
        	}
            
        }
        
        
       //return echoMsg;
    }
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
 
}