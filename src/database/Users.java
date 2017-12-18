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
		
//		users.add(new UserInfo("aaa", "123", "ȫ����", " "));
//		users.add(new UserInfo("bbb", "123", "������", " "));
//		users.add(new UserInfo("ccc", "123", "���ȯ", " "));
//		users.add(new UserInfo("ddd", "123", "������", " "));
//		users.add(new UserInfo("eee", "123", "���¼�", " "));
//		users.add(new UserInfo("fff", "123", "�ֿ���", " "));

		users.add(new UserInfo("ȫ����", "123", "ȫ����", " "));
		users.add(new UserInfo("������", "123", "������", " "));
		users.add(new UserInfo("���ϴ�", "123", "���ϴ�", " "));
		users.add(new UserInfo("�̿���", "123", "�̿���", " "));
		users.add(new UserInfo("�ֿ���", "123", "�ֿ���", " "));
		users.add(new UserInfo("���¼�", "123", "���¼�", " "));
		
		
		for(int i=0; i<users.size(); i++) {
			for(int j=0; j<users.size(); j++) {
				if(i != j)
					users.get(i).addFriends(users.get(j));
			}
			users.get(i).setStateMessage(users.get(i).getName() + "�� ���¸޼��� �Դϴ�");
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
