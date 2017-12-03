package chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import database.UserInfo;
import protocol.Protocol;

public class ChatRoom implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7033408661297230161L;
	private int roomId;
	private String names;
	private Vector<String> chatMessages = null;
	
	private transient Vector<UserInfo> users = null;
	private transient Vector<DataOutputStream> dataOutputStreams = null;
	private transient Vector<Integer> latestReadMessageNums = null;
	private transient DataOutputStream logoutState = null;
	
	public ChatRoom() {  };

	public ChatRoom(int roomId, UserInfo user1, UserInfo user2) {
		this.roomId = roomId;
		users = new Vector<UserInfo>();
		dataOutputStreams = new Vector<DataOutputStream>();
		latestReadMessageNums = new Vector<Integer>();
		chatMessages = new Vector<String>();
		String ids[] = { user1.getId(), user2.getId() };
		Arrays.sort(ids);
		names = ids[0] + "," + ids[1];
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
		if (this.roomId == roomId)
			return true;
		return false;
	}

	public void addLatestReadMessageNums() {
		latestReadMessageNums.add(chatMessages.size());
	}

	public void createChatRoom(int roomId, String msg, String names) {
		this.names = names;
		String time = getTime();
		debug.Debug.log("yunjae time = " + time);
		msg = msg + "::::" + time;
		chatMessages.add(msg);
		debug.Debug.log("Createchatroom dataOutputStreams size = " + dataOutputStreams.size());
		for (int i = 0; i < users.size(); i++) {
			if (dataOutputStreams.get(i) == null) {

			} else {
				try {
					dataOutputStreams.get(i).writeInt(Protocol.CHAT_ROOM_RESPONSE);
					dataOutputStreams.get(i).writeInt(roomId);
					dataOutputStreams.get(i).writeUTF(names);
					dataOutputStreams.get(i).writeInt(Protocol.MSG_RELAY);
					dataOutputStreams.get(i).writeUTF(msg);
				} catch (IOException e) {
					debug.Debug.log(e.getMessage());
				}
			}
		}
		//debug.Debug.log(chatMessages.toString());
	}

	public void requestMsg(String msg) {
		String time = getTime();
		msg = msg + "::::" + time;
		chatMessages.add(msg);
		for (int i = 0; i < users.size(); i++) {
//			if (dataOutputStreams.get(i).equals(logoutState)) {
//
//			} else {
				try {
					dataOutputStreams.get(i).writeInt(Protocol.MSG_RELAY);
					dataOutputStreams.get(i).writeUTF(msg);
				} catch (IOException e) {
					debug.Debug.log(e.getMessage());
				}
//			}
		}
		//debug.Debug.log(chatMessages.toString());
	}

	public void addUser(int roomId, String names) {
		for (int i = 0; i < users.size(); i++) {
			if (dataOutputStreams.get(i).equals(logoutState)) {

			} else {
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
	
	public String getTime() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dayTime.format(new Date(time));
		return str;
	}
	
	public String getNames() {
		return names;
	}
	
	public void setNames(String names) {
		this.names = names;
	}

	public int getRoomId() {
		return roomId;
	}

}
