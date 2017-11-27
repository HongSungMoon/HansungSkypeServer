package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

import database.Users;

public class Server extends Thread {

	public static final String ServerIP = "127.0.0.1";
	public static final int ServerPort = 9000;
	public static Users users;

	private ServerSocket listener = null;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private Vector<ResponseServer> responseServers;

	public Server() {

		users = new Users();
		responseServers = new Vector<ResponseServer>();
		listenerInit();

	}

	public void listenerInit() {

		try {
			listener = new ServerSocket(ServerPort);
		} catch (IOException e) {
			debug.Debug.log("ServerSocket Create Error");
		}

	}

	public void run() {
		
		while(true) {
			try {
				socket = listener.accept();
			} catch (IOException e) {
				debug.Debug.log("Listener Accept Error");
			}
			ResponseServer responseServer = new ResponseServer(socket, this);
			responseServers.add(responseServer);
			responseServer.start();
		}

	}
	
	public void loginRequest(String id) {
		for(int i=0; i<responseServers.size(); i++) {
			responseServers.get(0).loginRequest(id);
		}
	}
	
	public void broadcastProtocol(int protocol, String msg) {
		for(int i=0; i<responseServers.size(); i++) {
			responseServers.get(0).broadcastProtocol(protocol, msg);
		}
	}

	public void removeResponseServer(ResponseServer responseServer) {
		for(int i=0; i<responseServers.size(); i++) {
			if(responseServers.get(i).equals(responseServer))
				responseServers.remove(i);
		}
	}
	
	public Users getUsers() {
		return users;
	}

}
