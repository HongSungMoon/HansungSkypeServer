package database;

import java.util.Vector;

import database.UserInfo;

public class Users {
	
	private  Vector<UserInfo> users = new Vector<UserInfo>();
	private  Vector<UserInfo> loginUsers = new Vector<UserInfo>();
	
	public Users() {
		userInit();
	}

	public  void userInit() {
		
		users.add(new UserInfo("aaa", "123", "홍성문", " "));
		users.add(new UserInfo("bbb", "123", "이윤재", " "));
		users.add(new UserInfo("ccc", "123", "백승환", " "));
		users.add(new UserInfo("ddd", "123", "이태윤", " "));
		users.add(new UserInfo("eee", "123", "민태성", " "));
		users.add(new UserInfo("fff", "123", "최원균", " "));
		
		for(int i=0; i<users.size(); i++) {
			for(int j=0; j<users.size(); j++) {
				if(i != j)
					users.get(i).addFriends(users.get(j));
			}
			users.get(i).setStateMessage(users.get(i).getName() + "의 상태메세지 입니다");
		}

	}
	
	synchronized public  Vector<UserInfo> loginRequest(String id, String ip) {
		UserInfo user = getUser(id);
		if(user != null) {
			user.setIp(ip);
			user.setConnectionState(true);
			loginUsers.add(user);
			return loginUsers;
		}
		return null;
	}
	
	synchronized public UserInfo getUser(String id) {
		
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getId().equals(id)) 
				return users.get(i);
		}
		return null;
		
	}
	
	synchronized public  UserInfo getLoginUser(String id) {
		for(int i=0; i<loginUsers.size(); i++) {
			if(loginUsers.get(i).getId().equals(id)) 
				return loginUsers.get(i);
		}
		return null;
	}
	
	public void autoAddFriends() {
		
	}
	
}
