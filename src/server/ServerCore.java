package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controler.HandleClient;
import media.EmissionSong;
import model.Client;
import model.ShoutcastModel;
import viewUser.ShoutcastOutput;

public class ServerCore extends Thread {
	private int port; // port HTTP
	private int n = 0;

	private boolean stop;
	private ILogger logger;

	private BufferedReader input;
	private PrintWriter output;
	private EmissionSong es;

	public ServerCore(int port,EmissionSong es) throws IOException {
		this.port = port; // port HTTP
		this.stop = false;
		logger = new TextLogger();
		logger.systemMessage("Server started...");
		this.es = es;
	}

	public void run() {
		try (ServerSocket ss = new ServerSocket(port)) {
			ss.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket s = ss.accept();
					logger.clientConnected(s.toString());
					// 
					BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String res;
					res = "HTTP/1.1 200 OK\r\n"
							+ "icy-name: Myradio\r\n"
//							+ "icy-genre:\r\n"
					
							+ "content-type: audio/mpeg\r\n"
							+ "icy-pub: 1\r\n"
							+ "icy-br: 320\r\n"
							+ "icy-metaint: 40000\r\n"
							+ "\r\n";
	
					byte[] b1 = new byte[res.length()];
					for(int i = 0; i < res.length();i++){
						b1[i] = (byte) res.charAt(i);
					}
					String result, httpreq = "";
					while((result = is.readLine()).length() != 0) {
						System.out.println(result);
						httpreq = httpreq + result;
					}
					
					Client c = new Client(s.getInetAddress().getHostAddress(), httpreq, n, s.getOutputStream());
					n++;
					
					s.getOutputStream().write(b1);
					ShoutcastModel.registerClient(c);

				} catch (SocketTimeoutException ex) {

				}
			}
		} catch (IOException e) {
			System.out.println("Could not bind port " + port);
			Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public synchronized void finish() {
		stop = true;
//		try {
//			input.close();
//			output.close();
//		} catch (IOException e) {
//
//		}
	}

	public void sendData(String data) {
		output.println(data);
		output.flush();
	}

	public String receiveData() {
		String data = "";
		try {
			data = input.readLine();
		} catch (IOException e) {

		}
		return data;
	}

}
