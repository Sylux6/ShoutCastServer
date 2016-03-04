package Server;

import java.io.IOException;

public class ServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerCore core = new ServerCore();
			core.start();
			
		} catch(IOException e){
			System.out.println("Error during initialisation:"+e.toString());
		}
	}

}
