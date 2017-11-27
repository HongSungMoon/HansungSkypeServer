package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
	private String buffer;
	private int protocal;
	private String id;
	private String ip;
	private String pw;
	private Vector<UserInfo> users;
	private Users usersClass;
	private Server server;

	public ResponseServer(Socket socket, Server server) {
		this.socket = socket;
		this.ip = socket.getInetAddress().toString();
		this.server = server;
		usersClass = server.getUsers();
		streamInit();
		debug.Debug.log("ID : " + id + " IP : " + ip + "  ResponseServer Create - Login");
	}

	public void streamInit() {
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			String msg = dataInputStream.readUTF();
			String loginInfo[] = msg.split(",");
			id = loginInfo[0];
			pw = loginInfo[1];
		} catch (IOException e) {
			debug.Debug.log("Data Input/Output Stream Init Error");
		}
	}

	public void run() {

		users = Server.users.loginRequest(id, ip);
		if (users == null)
			loginFail();
		server.loginRequest(id);

		while (true) {

			try {
				protocal = dataInputStream.readInt();

				switch (protocal) {
				case Protocol.CLIENT_LOGIN:
					buffer = dataInputStream.readUTF();
					debug.Debug.log("ResponseServer : Client_Login  id : " + buffer);
					break;
				}
			} catch (IOException e) {
				close();
				return;
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
			dataOutputStream.writeInt(Protocol.LOGIN_SUCCESS);
			if (this.id.equals(id)) {
				UserInfo user = Server.users.getUser(id);
				objectOutputStream.writeObject(user);
				objectOutputStream.writeObject(users);
				debug.Debug.log("ID : " + id + " IP : " + ip + "  LoginSuccess");
			} else
				dataOutputStream.writeUTF(id + "," + socket.getInetAddress().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			server.removeResponseServer(this);
			//server.broadcastProtocol(Protocol.CLIENT_LOGOUT, id);
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
		usersClass.getUser(id).setConnectionState(true);
	}

}
