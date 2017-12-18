package database;

import java.net.InetAddress;
import java.util.Vector;

import database.UserInfo;

public class Users {
	
	private  Vector<UserInfo> users = new Vector<UserInfo>();
	private  Vector<UserInfo> loginUsers = new Vector<UserInfo>();
	
	public Users() {
		userInit();
	}

	public  void userInit() {
		
//		users.add(new UserInfo("aaa", "123", "È«¼º¹®", " "));
//		users.add(new UserInfo("bbb", "123", "ÀÌÀ±Àç", " "));
//		users.add(new UserInfo("ccc", "123", "¹é½ÂÈ¯", " "));
//		users.add(new UserInfo("ddd", "123", "ÀÌÅÂÀ±", " "));
//		users.add(new UserInfo("eee", "123", "¹ÎÅÂ¼º", " "));
//		users.add(new UserInfo("fff", "123", "ÃÖ¿ø±Õ", " "));

		users.add(new UserInfo("È«¼º¹®", "123", "È«¼º¹®", " "));
		users.add(new UserInfo("ÀÌÀ±Àç", "123", "ÀÌÀ±Àç", " "));
		users.add(new UserInfo("±èÇÏ´Ã", "123", "±èÇÏ´Ã", " "));
		users.add(new UserInfo("ÀÌ¿¹Áö", "123", "ÀÌ¿¹Áö", " "));
		users.add(new UserInfo("ÃÖ¿ø±Õ", "123", "ÃÖ¿ø±Õ", " "));
		users.add(new UserInfo("¹ÎÅÂ¼º", "123", "¹ÎÅÂ¼º", " "));
		
		
		for(int i=0; i<users.size(); i++) {
			for(int j=0; j<users.size(); j++) {
				if(i != j)
					users.get(i).addFriends(users.get(j));
			}
			users.get(i).setStateMessage(users.get(i).getName() + "ÀÇ »óÅÂ¸Ş¼¼Áö ÀÔ´Ï´Ù");
		}

	}
	
	synchronized public  Vector<UserInfo> loginRequest(String id, String pw, InetAddress ip) {
		UserInfo user = getUser(id, pw);
		if(user != null) {
			user.setIp(ip);
			user.setConnectionState(true);
			loginUsers.add(user);
			return loginUsers;
		}
		return null;
	}
	
	synchronized public UserInfo getUser(String id, String pw) {
		
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getId().equals(id) && users.get(i).getPw().equals(pw)) 
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
	
	public Vector<UserInfo> getUsers() {
		return users;
	}
	
}
