package Server;

import java.io.IOException;

public class ServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = args.length==1?Integer.parseInt(args[0]): 1234;
		try {
			ServerCore core = new ServerCore(port);
			core.start();
			
		} catch(IOException e){
			System.out.println("Error during initialisation:"+e.toString());
		}
	}

}
