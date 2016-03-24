package server;

import java.util.Date;

public class TextLogger implements ILogger {

	@Override
	public void clientConnected(String ip) {
		// TODO Auto-generated method stub
		System.out.println(ip + " has been connected");
	}

	@Override
	public void clientDisconnected(String ip) {
		// TODO Auto-generated method stub
		System.out.println(ip + " has been disconnected");
	}

	@Override
	public void systemMessage(String msg) {
		// TODO Auto-generated method stub
		System.out.println("System Message: "+ msg);
	}

	@Override
	public void currentMusic(String musique) {
		// TODO Auto-generated method stub
		System.out.println("Song at "+new Date().getTime()+": "+musique);
		
	}

}
