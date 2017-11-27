package database;

import java.util.Vector;

import server.UserInfo;

public class Users {
	
	private static Vector<UserInfo> users = new Vector<UserInfo>();
	private static Vector<UserInfo> loginUsers = new Vector<UserInfo>();
	
	public Users() {
		Users.userInit();
	}

	public static void userInit() {

		users.add(new UserInfo("È«¼º¹®"));
		users.add(new UserInfo("ÀÌÀ±Àç"));
		users.add(new UserInfo("¹é½ÂÈ¯"));
		users.add(new UserInfo("ÀÌÅÂÀ±"));
		users.add(new UserInfo("¹ÎÅÂ¼º"));
		users.add(new UserInfo("ÃÖ¿ø±Õ"));

	}
	
	synchronized public static Vector<UserInfo> loginRequest(String id, String ip) {
		UserInfo user = getUser(id);
		if(user != null) {
			user.setId(ip);
			user.setConnectionState(true);
			loginUsers.add(user);
			return loginUsers;
		}
		return null;
	}
	
	synchronized public static UserInfo getUser(String id) {
		
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getName().equals(id)) 
				return users.get(i);
		}
		return null;
		
	}
	
	synchronized public static UserInfo getLoginUser(String id) {
		for(int i=0; i<loginUsers.size(); i++) {
			if(loginUsers.get(i).getName().equals(id)) 
				return loginUsers.get(i);
		}
		return null;
	}
	
}
