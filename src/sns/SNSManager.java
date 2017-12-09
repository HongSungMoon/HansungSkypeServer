package sns;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import server.Server;

public class SNSManager{

	private Vector<SNS> snss;
	private Server server;
	
	public SNSManager(Server server) {
		this.server = server;
		snss = new Vector<SNS>();
	}
	
	public void addSNS(SNS sns) {
		if(snss.size() > 5 ) snss.remove(0);
		sns.setDate(getTime());
		this.snss.add(sns);
		debug.Debug.log("¼­¹ö À±Àç addSNS : " + snss.size());
	}
	
	public Vector<SNS> getSNS() {
		return snss;
	}
	
	public String getTime() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dayTime.format(new Date(time));
		//debug.Debug.log("Date = " + str);
		return str;
	}

}
