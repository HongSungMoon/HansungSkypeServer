package handling;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LoginHandling {
	
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputSream;
	
	public LoginHandling(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputSream) {
		this.socket = socket;
		this.dataInputStream = dataInputStream;
		this.dataOutputSream = dataOutputSream;
	}
	
	public void login() {
		
	}

}
