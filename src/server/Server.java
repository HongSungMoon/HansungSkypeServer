package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

import chat.ChatRoom;
import chat.ChatRoomManager;
import database.UserInfo;
import database.Users;
import session.Session;

public class Server extends Thread {

	public static final String ServerIP = "127.0.0.1";
	public static final int ServerPort = 9000;
	public static Users users;

	private ServerSocket listener = null;
	private Socket socket = null;
	
	private Vector<ResponseServer> responseServers = null;
	private ChatRoomManager chatRoomManager = null;
	private Vector<Session> sessions = null;

	public Server() { 

		users = new Users();
		responseServers = new Vector<ResponseServer>();
		chatRoomManager = new ChatRoomManager(this);
		sessions = new Vector<Session>();
		listenerInit();

	}

	public void listenerInit() {

		try {
			listener = new ServerSocket(ServerPort);
		} catch (IOException e) {
			debug.Debug.log("ServerSocket Create Error");
		}

	}

	public void run() {

		while (true) {
			try {
				socket = listener.accept();
			} catch (IOException e) {
				debug.Debug.log("Listener Accept Error");
			}
			ResponseServer responseServer = new ResponseServer(socket, this);
			responseServers.add(responseServer);
			responseServer.start();
		}

	}

	public void loginRequest(String id, String pw) {
		for (int i = 0; i < responseServers.size(); i++) {
			responseServers.get(i).loginRequest(id, pw);
		}
	}

	public void broadcastProtocol(int protocol, String msg) {
		for (int i = 0; i < responseServers.size(); i++) {
			responseServers.get(i).broadcastProtocol(protocol, msg);
		}
	}
	
	public void removeResponseServer(ResponseServer responseServer) {
		for (int i = 0; i < responseServers.size(); i++) {
			if (responseServers.get(i).equals(responseServer))
				responseServers.remove(i);
		}
	}

	public Users getUsers() {
		return users;
	}
	
	public ResponseServer getResponseServer(String id) {
		for(int i=0; i<responseServers.size(); i++) {
			if(responseServers.get(i).getUserId().equals(id))
				return responseServers.get(i);
		}
		return null;
	}

	public DataOutputStream getDataOutputStream(UserInfo user) {
		String id = user.getId();
		for (int i = 0; i < responseServers.size(); i++) {
			if (responseServers.get(i).checkUser(id))
				return responseServers.get(i).getDataOutputStream();
		}
		return null;
	}

	public ChatRoom getChatRoom(int roomId) {
		return chatRoomManager.getChatRoom(roomId);
	}

	public void CreateChatRoom(UserInfo user1, UserInfo user2) {
		chatRoomManager.createChatRoom(user1, user2);
	}
	
	public void CreateChatRoom(UserInfo user1, UserInfo user2, UserInfo user3) {
		chatRoomManager.createChatRoom(user1, user2, user3);
	}

	public void addSession(ResponseServer user1, ResponseServer user2) {
		Session session = new Session(this, user1, user2);
		sessions.add(session);
	}
	
	public void getUser(String id, String pw) {

	}

	public int getRoomId(String names) {
		return chatRoomManager.getRoomId(names);
	}

	public Vector<ChatRoom> getConversationList(String name) {
		return chatRoomManager.getConversationList(name);
	}
	
	public ChatRoomManager getChatRoomManager() {
		return chatRoomManager;
	}
	
	public void addSession(Session session) {
		sessions.add(session);
	}
	
	public void removeSession(Session session) {
		for(int i=0; i<sessions.size(); i++) {
			if(sessions.get(i).equals(session))
				sessions.remove(i);
		}
	}
	
}
