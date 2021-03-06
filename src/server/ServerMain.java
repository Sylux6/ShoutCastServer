package server;

import java.io.IOException;

import admin.PlayListAdminWin;
import media.FluxAudio;

public class ServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = args.length==1?Integer.parseInt(args[0]): 1234;
		try {
			FluxAudio pl = new FluxAudio();
			ServerCore core = new ServerCore(port,pl);
			new PlayListAdminWin(pl);
			core.start();
			pl.start();
			
		} catch(IOException e){
			System.out.println("Error during initialisation:"+e.toString());
		}
	}
	
}
