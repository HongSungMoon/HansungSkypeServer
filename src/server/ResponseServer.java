package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

import chat.ChatRoom;
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
	private int protocol;
	private String id;
	private InetAddress ip;
	private String pw;
	private Vector<UserInfo> loginUsers;
	private Server server;

	private String buffers[];
	private String ids[];

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
			int roomId;
			String buffers[];
			try {
				protocol = dataInputStream.readInt();
				debug.Debug.log("ResponseServer  id : " + id + "  Get : " + Integer.toString(protocol));
				switch (protocol) {
				case Protocol.CLIENT_LOGIN:
					buffer = dataInputStream.readUTF();
					InetAddress address = (InetAddress) objectInputStream.readObject();
					objectInputStream.reset();
					UserInfo connectClient = getUser(buffer);
					connectClient.setIp(address);
					debug.Debug.log("ResponseServer : Client_Login  id : " + buffer);
					break;
				case Protocol.CLIENT_LOGOUT:
					close();
					return;
				case Protocol.MSG_REQUEST:
					buffer = dataInputStream.readUTF();
					debug.Debug.log("ResponseServer : MSG_REQUEST  buffer : " + buffer);
					buffers = buffer.split("::::");
					ids = buffers[2].split(",");
					Arrays.sort(ids);
					roomId = server.getRoomId(ids[0] + "," + ids[1]);
					buffer = buffers[0] + "::::" + buffers[1] + "::::" + ids[0] + "," +ids[1] + "::::" + buffers[3];
					server.getChatRoom(roomId).requestMsg(buffer);
					break;
				case Protocol.CHAT_ROOM_REQUEST:
					buffer = dataInputStream.readUTF();
					buffers = buffer.split("::::");
					ids = buffers[1].split(",");
					Arrays.sort(ids);
					UserInfo user1 = getUser(ids[0]);
					UserInfo user2 = getUser(ids[1]);
					debug.Debug.log(buffer);
					server.CreateChatRoom(user1, user2);
					roomId = server.getRoomId(ids[0] + "," + ids[1]);
					buffer = buffers[0] + "::::" + ids[0] + "," +ids[1] + "::::" + buffers[2];
					String msg = Integer.toString(roomId) + "::::" + buffer;
					debug.Debug.log(msg);
					server.getChatRoom(roomId).createChatRoom(roomId, msg, ids[0] + "," + ids[1]);
					break;
				case Protocol.MSG_ADD_USER_REQUEST:
					
					break;
				case Protocol.CONVERSATION_REQUEST:
					String name = dataInputStream.readUTF();
					Vector<ChatRoom> rooms = server.getConversationList(name);
					for(int i=0; i<rooms.size(); i++) {
						debug.Debug.log(rooms.get(i).getNames());
					}
					dataOutputStream.writeInt(Protocol.CONVERSATION_RESPONSE);
					objectOutputStream.writeObject(rooms);
					debug.Debug.log(rooms.toString());
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

	public Vector<UserInfo> getLoginUsers() {
		return loginUsers;
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
				debug.Debug.log("ID : " + id + "  ClientLogin");
				dataOutputStream.writeInt(Protocol.CLIENT_LOGIN);
				dataOutputStream.writeUTF(id);
				objectOutputStream.writeObject(ip);
			}

		} catch (IOException e) {
			debug.Debug.log("ID : " + id + " IP : " + ip + "  LoginFail");
		}
	}

	public void close() {
		try {
			server.removeResponseServer(this);
			server.broadcastProtocol(Protocol.CLIENT_LOGOUT, id);
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
		for (int i = 0; i < Server.users.getUsers().size(); i++) {
			if (Server.users.getUsers().get(i).getId().equals(id))
				return Server.users.getUsers().get(i);
		}
		return null;
	}

	public void printVector() {
		for (int i = 0; i < Server.users.getUsers().size(); i++) {
			System.out.println(" " + i + " " + Server.users.getUsers().get(i).getId());
		}
	}

	public boolean checkUser(String id) {
		if (this.id.equals(id)) {
			return true;
		}
		return false;
	}

	public DataOutputStream getDataOutputStream() {

		return dataOutputStream;

	}

}
