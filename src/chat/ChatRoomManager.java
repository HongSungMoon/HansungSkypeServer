package chat;

import java.io.DataOutputStream;
import java.util.Vector;

import database.UserInfo;
import server.Server;

public class ChatRoomManager {
	
	private int roomId;
	private Vector<ChatRoom> rooms;
	private Server server;
	
	public ChatRoomManager(Server server) {
		
		this.server = server;
		roomId = 0;
		rooms = new Vector<ChatRoom>();
		
	}
	
	public void createChatRoom(UserInfo user1, UserInfo user2) {
		
		ChatRoom chatRoom = new ChatRoom(roomId, user1, user2);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user1));
		chatRoom.addDataOutputStream(server.getDataOutputStream(user2));
		chatRoom.addLatestReadMessageNums();
		chatRoom.addLatestReadMessageNums();
		chatRoom.notifyMadeRoom();
		roomId++;
		rooms.add(chatRoom);
	}
	
	public void addChatRoomUser(int roomId, UserInfo user) {
		
		ChatRoom chatRoom = rooms.get(roomId);
		chatRoom.addUser(user);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user));
		chatRoom.addLatestReadMessageNums();
		
	}
	
	public DataOutputStream getDataOutputStream(UserInfo user) {
		DataOutputStream dataOutputStraem = server.getDataOutputStream(user);
		return dataOutputStraem;
	}

	public ChatRoom getChatRoom(int roomId) {
		for(int i=0; i<rooms.size(); i++) {
			if(rooms.get(i).checkChatRoomId(roomId))
				return rooms.get(i);
		}
		return null;
	}

}
