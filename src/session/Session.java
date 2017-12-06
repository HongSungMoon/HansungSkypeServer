package session;

import java.net.MulticastSocket;
import java.util.Vector;

import database.UserInfo;
import server.ResponseServer;

public class Session {
	
	private Vector<UserInfo> users;
	private final int startPort = 9001;
	private Vector<ResponseServer> audioUsers;
	
	public Session(UserInfo user1, UserInfo user2) {
		users = new Vector<UserInfo>();
		audioUsers = new Vector<ResponseServer>();
		users.add(user1);
		users.add(user2);
	}
	
	public void addUser(UserInfo user) {
		users.add(user);
	}
	
	public Vector<UserInfo> getUsers() {
		return users;
	}
	
	public int getUsersCount() {
		return users.size();
	}
	
	

}
