package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controler.HandleClient;
import Model.ShoutcastModel;

public class ServerCore extends Thread{
	private int port; //port HTTP
	private boolean stop;
	private ILogger logger;
	
	private BufferedReader input;
	private PrintWriter output;
	
	public ServerCore(int port) throws IOException {
		this.port = port; //port HTTP
		this.stop = false;
		logger = new TextLogger();
		logger.systemMessage("Server started...");
	}
	
	public void run() {
		try (ServerSocket ss = new ServerSocket(port)) {
			ss.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket s = ss.accept();
					logger.clientConnected(s.toString());
					new Thread(new HandleClient(s, logger)).start();
				} catch (SocketTimeoutException ex) {
				}
			}
		} catch (IOException e) {
			System.out.println("Could not bind port " + port);
			Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE,null, e);
		}
	}
	
	public synchronized void finish() {
		stop = true;
		try {
			input.close();
			output.close();
		} catch(IOException e) {
			
		}
	}
	
	public void sendData(String data) {
		output.println(data);
		output.flush();
	}
	
	public String receiveData() {
		String data = "";
		try {
			data = input.readLine();
		} catch(IOException e) {
			
		}
		return data;
	}
	
}
