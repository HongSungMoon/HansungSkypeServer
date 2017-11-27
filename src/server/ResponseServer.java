package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import database.Users;
import protocol.Protocol;

public class ResponseServer extends Thread {

	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private String buffer;
	private int protocal;
	private String id;
	private String ip;
	private Vector<UserInfo> loginUser;
	private Vector<Socket> loginClients;
	private Server server;

	public ResponseServer(Socket socket, Server server) {
		this.socket = socket;
		this.ip = socket.getInetAddress().toString();
		this.server = server;
		streamInit();
		debug.Debug.log("ID : " + id + " IP : " + ip + "ResponseServer Create - Login");
	}

	public void streamInit() {
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			id = dataInputStream.readUTF();
		} catch (IOException e) {
			debug.Debug.log("Data Input/Output Stream Init Error");
		}
	}

	public void run() {

		loginUser = Users.loginRequest(id, ip);
		if (loginUser == null)
			loginFail();
		server.loginRequest(id);

		while (true) {

			try {
				protocal = dataInputStream.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			switch (protocal) {
			case 1:
				debug.Debug.log("ResponseServer Receive MSG : " + buffer);
				break;
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

	public void loginRequest(String id) {
		try {
			dataOutputStream.writeInt(Protocol.LOGIN_SUCCESS);
			dataOutputStream.writeUTF(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();
			dataInputStream.close();
			dataOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		interrupt();
	}

}
