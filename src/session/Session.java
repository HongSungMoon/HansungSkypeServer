package session;

import java.util.Vector;

import database.UserInfo;

public class Session {
	
	private Vector<UserInfo> users;
	
	public Session(UserInfo user1, UserInfo user2) {
		users = new Vector<UserInfo>();
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
