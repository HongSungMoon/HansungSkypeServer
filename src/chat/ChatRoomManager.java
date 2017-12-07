package chat;

import java.io.DataOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import database.UserInfo;
import server.Server;

public class ChatRoomManager {

	private int roomId;
	private Vector<ChatRoom> rooms;
	private Server server;
	private HashMap<String, Integer> map;

	public ChatRoomManager(Server server) {

		this.server = server;
		roomId = 0;
		rooms = new Vector<ChatRoom>();
		map = new HashMap<String, Integer>();

	}
	
	public void createChatRoom(UserInfo user1, UserInfo user2, UserInfo user3) {

		ChatRoom chatRoom = new ChatRoom(roomId, user1, user2, user3);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user1));
		chatRoom.addDataOutputStream(server.getDataOutputStream(user2));
		chatRoom.addDataOutputStream(server.getDataOutputStream(user3));
		chatRoom.addLatestReadMessageNums();
		chatRoom.addLatestReadMessageNums();
		chatRoom.addLatestReadMessageNums();
		// chatRoom.notifyMadeRoom();
		
		String[] members = { user1.getId(), user2.getId(), user3.getId() };
		Arrays.sort(members);
		String member = members[0] + "," + members[1] + "," + members[2];
		chatRoom.setNames(member);
		map.put(member, roomId);
		roomId++;
		rooms.add(chatRoom);
//		for(int i=0; i<rooms.size(); i++) {
//			debug.Debug.log("rooms.get : " + i + " " + rooms.get(i).getNames());
//		}
//
//		debug.Debug.log("CreateChatRoom roomId : " + (roomId-1) + "  member : " + member);
	}

	public void createChatRoom(UserInfo user1, UserInfo user2) {

		ChatRoom chatRoom = new ChatRoom(roomId, user1, user2);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user1));
		chatRoom.addDataOutputStream(server.getDataOutputStream(user2));
		chatRoom.addLatestReadMessageNums();
		chatRoom.addLatestReadMessageNums();
		// chatRoom.notifyMadeRoom();
		
		String[] members = { user1.getId(), user2.getId() };
		Arrays.sort(members);
		String member = members[0] + "," + members[1];
		chatRoom.setNames(member);
		map.put(member, roomId);
		roomId++;
		rooms.add(chatRoom);
//		for(int i=0; i<rooms.size(); i++) {
//			debug.Debug.log("rooms.get : " + i + " " + rooms.get(i).getNames());
//		}
//
//		debug.Debug.log("CreateChatRoom roomId : " + (roomId-1) + "  member : " + member);
	}

	public void addChatRoomUser(String mapIds, UserInfo user) {

		debug.Debug.log("old names : " + mapIds + "   roomId : " + map.get(mapIds));
		int roomId = map.get(mapIds);
		ChatRoom chatRoom = rooms.get(roomId);
		chatRoom.addUser(user);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user));
		chatRoom.addLatestReadMessageNums();
		map.remove(mapIds);
		String names = mapIds + "," + user.getId();
		String name[] = names.split(",");
		Arrays.sort(name);
		for (int i = 0; i < name.length; i++) {
			if (i == 0)
				names = name[i];
			else
				names = names + "," + name[i];
		}
		map.put(names, roomId);
		debug.Debug.log("new names : " + names + "   roomId : " + map.get(names));
		chatRoom.setNames(names);
	}
	
	public void addChatRoomUser(String mapIds, UserInfo user, String inviteName) {

		debug.Debug.log("old names : " + mapIds + "   roomId : " + map.get(mapIds));
		int roomId = map.get(mapIds);
		ChatRoom chatRoom = rooms.get(roomId);
		chatRoom.addUser(user);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user));
		chatRoom.addLatestReadMessageNums();
		map.remove(mapIds);
		String names = mapIds + "," + user.getId();
		String name[] = names.split(",");
		Arrays.sort(name);
		for (int i = 0; i < name.length; i++) {
			if (i == 0)
				names = name[i];
			else
				names = names + "," + name[i];
		}
		map.put(names, roomId);
		debug.Debug.log("new names : " + names + "   roomId : " + map.get(names));
		chatRoom.setNames(names);
		String msg = Integer.toString(roomId) + "::::" + inviteName + "::::" + names + "::::" + inviteName + "님께서 " + 
		user.getId() + "님을 초대하였습니다";
		chatRoom.addMultiUser(mapIds, names, msg);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chatRoom.requestMsg(msg);
	}
	
	public void addLoginChatRoomUser(String mapIds, UserInfo user) {

		ChatRoom chatRoom = rooms.get(roomId);
		chatRoom.addUser(user);
		chatRoom.addDataOutputStream(server.getDataOutputStream(user));
		chatRoom.addLatestReadMessageNums();
		debug.Debug.log("addLoginChatRoomUser  roomId : " + rooms.get(roomId) + "  id : " + user.getId());
		map.put(mapIds, roomId);

	}

	public DataOutputStream getDataOutputStream(UserInfo user) {
		DataOutputStream dataOutputStraem = server.getDataOutputStream(user);
		return dataOutputStraem;
	}

	public ChatRoom getChatRoom(String names) {
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getNames().equals(names))
				return rooms.get(i);
		}
		return null;
	}
	
	public ChatRoom getChatRoom(int roomId) {
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId() == roomId)
				return rooms.get(i);
		}
		return null;
	}

	public int getRoomId(String names) {
		if (!map.containsKey(names))
			return -1;
		return map.get(names);
	}

	public Vector<ChatRoom> getConversationList(String name) {
		Vector<ChatRoom> containRooms = new Vector<ChatRoom>();
		Iterator<String> keys = map.keySet().iterator();
        while( keys.hasNext() ){
            String key = keys.next();
            if(key.contains(name)) {
            	containRooms.add(getChatRoom(map.get(key)));
            	debug.Debug.log(name);
            }
        }
		return containRooms;
	}

}
