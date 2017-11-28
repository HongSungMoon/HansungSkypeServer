package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import database.UserInfo;
import database.Users;
import protocol.Protocol;

public class ResponseServer extends Thread {

	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;
	private String buffer;
	private int protocal;
	private String id;
	private InetAddress ip;
	private String pw;
	private Vector<UserInfo> loginUsers;
	private Server server;

	public ResponseServer(Socket socket, Server server) {
		this.socket = socket;
		this.ip = socket.getInetAddress();
		this.server = server;
		streamInit();
		debug.Debug.log("ID : " + id + " IP : " + ip + "  ResponseServer Create - Login");
	}

	public void streamInit() {
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			String msg = dataInputStream.readUTF();
			String loginInfo[] = msg.split(",");
			id = loginInfo[0];
			pw = loginInfo[1];
		} catch (IOException e) {
			debug.Debug.log("Data Input/Output Stream Init Error");
		}
	}

	public void run() {

		loginUsers = Server.users.loginRequest(id, ip);
		if (loginUsers == null)
			loginFail();
		server.loginRequest(id);

		while (true) {

			try {
				protocal = dataInputStream.readInt();

				switch (protocal) {
				case Protocol.CLIENT_LOGIN:
					buffer = dataInputStream.readUTF();
					InetAddress address = (InetAddress) objectInputStream.readObject();
					UserInfo connectClient = getUser(buffer);
					connectClient.setIp(address);
					debug.Debug.log("ResponseServer : Client_Login  id : " + buffer);
					break;
				}
			} catch (IOException e) {
				close();
				return;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void loginFail() {
		try {
			dataOutputStream.writeInt(Protocol.LOGIN_FAIL);
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void broadcastProtocol(int protocol, String msg) {
		try {
			dataOutputStream.writeInt(protocol);
			dataOutputStream.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loginRequest(String id) {
		try {
			if (this.id.equals(id)) {
				dataOutputStream.writeInt(Protocol.LOGIN_SUCCESS);
				UserInfo connectClient = Server.users.getUser(id);
				connectClient.setIp(ip);
				objectOutputStream.writeObject(connectClient);
				objectOutputStream.writeObject(Server.users.getUsers());
				debug.Debug.log("ID : " + id + " IP : " + ip + "  LoginSuccess");
			} else {
				dataOutputStream.writeInt(Protocol.CLIENT_LOGIN);
				dataOutputStream.writeUTF(id);
				objectOutputStream.writeObject(ip);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			server.removeResponseServer(this);
			// server.broadcastProtocol(Protocol.CLIENT_LOGOUT, id);
			socket.close();
			dataInputStream.close();
			dataOutputStream.close();
			debug.Debug.log("ID : " + id + " IP : " + ip + "  ResponseServer Create - LogOut");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.interrupt();
	}

	public void clientLogin(String id) {
		Server.users.getUser(id).setConnectionState(true);
	}
	
	public UserInfo getUser(String id) {
		for(int i=0; i<Server.users.getUsers().size(); i++) {
			if(Server.users.getUsers().get(i).getId().equals(id))
				return Server.users.getUsers().get(i);
		}
		return null;
	}
	
	public void printVector() {
		for (int i = 0; i < Server.users.getUsers().size(); i++) {
			System.out.println(" " + i + " " + Server.users.getUsers().get(i).getId());
		}
	}

}
