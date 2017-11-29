package chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import database.UserInfo;
import protocol.Protocol;

public class ChatRoom {
	
	private int roomId;
	private Vector<UserInfo> users = null; 
	private Vector<DataOutputStream> dataOutputStreams = null;
	private Vector<Integer> latestReadMessageNums = null;
	private Vector<String> chatMessages = null;
	private int totalChatCount;
	private DataOutputStream logoutState;
	
	public ChatRoom(int roomId, UserInfo user1, UserInfo user2)  {
		this.roomId = roomId;
		users = new Vector<UserInfo>();
		dataOutputStreams = new Vector<DataOutputStream>();
		latestReadMessageNums = new Vector<Integer>();
		chatMessages = new Vector<String>();
		totalChatCount = 0;
		users.add(user1);
		users.add(user2);
	}
	
	public void addUser(UserInfo user) {
		users.add(user);
	}
	
	public void addDataOutputStream(DataOutputStream dataOutputStream) {
		dataOutputStreams.add(dataOutputStream);
	}
	
	public boolean checkChatRoomId(int roomId) {
		if(this.roomId == roomId) 
			return true;
		return false;
	}
	
	public void addLatestReadMessageNums() {
		latestReadMessageNums.add(totalChatCount);
	}
	
	public void requestMsg(String msg) {
		chatMessages.add(msg);
		for(int i=0; i<users.size(); i++) {
			if(dataOutputStreams.get(i).equals(logoutState)) {
				
			}
			else {
				try {
					dataOutputStreams.get(i).writeInt(Protocol.MSG_RELAY);
					dataOutputStreams.get(i).writeUTF(msg);
				} catch (IOException e) {
					debug.Debug.log(e.getMessage());
				}
			}
		}
	}

	public void notifyMadeRoom() {
		String names = null;
		for(int i=0; i<users.size(); i++) {
			if(i == 0)
				names = users.get(i).getName();
			names = names + "," + users.get(i).getName();
		}
		for(int i=0; i<users.size(); i++) {
			if(dataOutputStreams.get(i).equals(logoutState)) {
				
			}
			else {
				try {
					dataOutputStreams.get(i).writeInt(Protocol.CHAT_ROOM_RESPONSE);
					dataOutputStreams.get(i).writeInt(roomId);
					dataOutputStreams.get(i).writeUTF(names);
				} catch (IOException e) {
					debug.Debug.log(e.getMessage());
				}
			}
		}
	}

}
