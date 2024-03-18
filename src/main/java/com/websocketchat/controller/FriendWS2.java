package com.websocketchat.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.Gson;
import com.websocketchat.jedis.JedisHandleMessage;
import com.websocketchat.model.ChatMessage;
import com.websocketchat.model.State;


@CrossOrigin
@ServerEndpoint("/chatroom") // chat.jsp 第25行
public class FriendWS2 {                                                     // 並行(同時進行) Safe-thread ,  同步鎖定並非整個集合而是裡面的空間
	private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();
	Gson gson = new Gson();

	@OnOpen //連線開啟的時候 
	
	public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
		/* save the new user in the map */
		sessionsMap.put(userName, userSession);
		/* Sends all the connected users to the new user */      //其他人上線的推送通知
		Set<String> userNames = sessionsMap.keySet();      //將線上所有使用者都取出
		State stateMessage = new State("open", userName, userNames);     // model.state class   "open"給前端的action , 透過建構子包裝
		String stateMessageJson = gson.toJson(stateMessage);   // 透過Gson API , 將stateMessage轉換成Json格式 
		Collection<Session> sessions = sessionsMap.values();  // 取出Map裡面所有的session (第27行)
		for (Session session : sessions) {    //推送讓其他使用者知道有人上線, 好友列表刷新 
			if (session.isOpen()) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}

		String text = String.format("Session ID = %s, connected; userName = %s%nusers: %s", userSession.getId(),
				userName, userNames);
		System.out.println(text);
	}

	@OnMessage             
	public void onMessage(Session userSession, String message) {     // API 透過String message 傳入Json
		ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);  //gson還原成Java物件 ,  model.ChatMessage
		String sender = chatMessage.getSender(); //獲得發送者資訊
		String receiver = chatMessage.getReceiver();  //獲得接收者資訊
		
		if ("history".equals(chatMessage.getType())) {
			List<String> historyData = JedisHandleMessage.getHistoryMsg(sender, receiver); //取得歷史聊天紀錄 -> jedis.JedisHandleMessage class
			String historyMsg = gson.toJson(historyData);  //將historyData轉成Json格式存入historyMsg
			ChatMessage cmHistory = new ChatMessage("history", sender, receiver, historyMsg);  // 透過ChatMessage建構子包裝
			if (userSession != null && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(gson.toJson(cmHistory)); //轉成Json物件後 , 透過sendText送給前端
				System.out.println("history = " + gson.toJson(cmHistory)); 
				return;
			}
		}
		
		
		Session receiverSession = sessionsMap.get(receiver);
		if (receiverSession != null && receiverSession.isOpen()) {
			receiverSession.getAsyncRemote().sendText(message);
			userSession.getAsyncRemote().sendText(message);
			JedisHandleMessage.saveChatMessage(sender, receiver, message);
		}
		//JedisHandleMessage.saveChatMessage(sender, receiver, message); 如果對方不在線上 , 以留言方式只要把這行往下移
		System.out.println("Message received: " + message);
	}

	@OnError
	public void onError(Session userSession, Throwable e) {
		System.out.println("Error: " + e.toString());
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		String userNameClose = null;
		Set<String> userNames = sessionsMap.keySet();
		for (String userName : userNames) {
			if (sessionsMap.get(userName).equals(userSession)) {
				userNameClose = userName;
				sessionsMap.remove(userName);
				break;
			}
		}

		if (userNameClose != null) {
			State stateMessage = new State("close", userNameClose, userNames);
			String stateMessageJson = gson.toJson(stateMessage);
			Collection<Session> sessions = sessionsMap.values();
			for (Session session : sessions) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}

		String text = String.format("session ID = %s, disconnected; close code = %d%nusers: %s", userSession.getId(),
				reason.getCloseCode().getCode(), userNames);
		System.out.println(text);
	}
}
