package session;

import protocol.Protocol;
import server.ResponseServer;
import server.Server;

public class Calling {
	
	private Server server;
	private ResponseServer requestServer;
	private ResponseServer partnerServer;
	
	public Calling(Server server, ResponseServer requestServer, ResponseServer partnerServer) {
		this.server = server;
		this.requestServer = requestServer;
		this.partnerServer = partnerServer;
		partnerServer.dataOutputStreamWriteInt(Protocol.CALLING);
		partnerServer.dataOutputStreamWriteUTF(partnerServer.getUserId());
	}
	
	public void  run() {
		if(requestServer != null && partnerServer != null)
			server.addSession(new Session(server, requestServer, partnerServer));
	}
}
