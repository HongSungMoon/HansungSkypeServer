package session;

import java.net.MulticastSocket;
import java.util.Vector;

import database.UserInfo;
import protocol.Protocol;
import server.ResponseServer;
import server.Server;

public class Session {
	
	private final int startPort = 9001;
	private Vector<ResponseServer> responseServers;
	private Vector<Integer> ports;
	private int nextPort;
	private Server server;
	
	public Session(Server server, ResponseServer user1, ResponseServer user2) {
		this.server = server;
		nextPort = startPort;
		responseServers = new Vector<ResponseServer>();
		responseServers.add(user1);
		responseServers.add(user2);
		ports = new Vector<Integer>();
		ports.add(nextPort++);
		ports.add(nextPort++);
		user1.dataOutputStreamWriteInt(Protocol.CALL_RESPONSE);
		user1.dataOutputStreamWriteInt(ports.get(0));
		user1.dataOutputStreamWriteInt(ports.get(1));
		user1.objectOutputStreamWriteInt(responseServers.get(1).getUserAddress());
		user2.dataOutputStreamWriteInt(Protocol.CALL_RESPONSE);
		user2.dataOutputStreamWriteInt(ports.get(1));
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
		if(responseServers.size() < 2)
			removeSession();
	}
	
	public Session containUser(ResponseServer responseServer) {
		if(responseServers.contains(responseServer))
			return this;
		return null;
	}
	
	public boolean isSession(ResponseServer responseServer) {
		for(int i=0; i<responseServers.size(); i++) {
			if(responseServers.get(i).equals(responseServer))
				return true;
		}
		return false;
	}
	
	public void removeSession() {
		for(int i=0; i<responseServers.size(); i++) {
			responseServers.get(i).dataOutputStreamWriteInt(Protocol.CALL_DISCONNECT);
		}
		server.removeSession(this);
	}
	
}
