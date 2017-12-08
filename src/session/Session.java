package session;

import java.net.MulticastSocket;
import java.util.Vector;

import database.UserInfo;
import protocol.Protocol;
import server.ResponseServer;

public class Session {
	
	private final int startPort = 9001;
	private Vector<ResponseServer> responseServers;
	private Vector<Integer> ports;
	private int nextPort;
	
	public Session(ResponseServer user1, ResponseServer user2) {
		nextPort = startPort;
		responseServers = new Vector<ResponseServer>();
		responseServers.add(user1);
		responseServers.add(user2);
		ports.add(nextPort++);
		ports.add(nextPort++);
		user1.dataOutputStreamWriteInt(Protocol.CALL_RESPONSE);
		user1.dataOutputStreamWriteInt(ports.get(1));
		user1.objectOutputStreamWriteInt(responseServers.get(0).getUserAddress());
		user2.dataOutputStreamWriteInt(Protocol.CALL_RESPONSE);
		user2.dataOutputStreamWriteInt(ports.get(0));
		user2.objectOutputStreamWriteInt(responseServers.get(0).getUserAddress());
	}
	
	public void addUser(ResponseServer user) {
		responseServers.add(user);
		ports.add(nextPort++);
	}
	
	public void removeUser(ResponseServer user) {
		for(int i=0; i<responseServers.size(); i++) {
			if(responseServers.get(i).getUserId().equals(user.getUserId())) {
					responseServers.remove(i);
					ports.remove(i);
			}
		}
	}
	
	public Session containUser(ResponseServer responseServer) {
		if(responseServers.contains(responseServer))
			return this;
		return null;
	}

}
