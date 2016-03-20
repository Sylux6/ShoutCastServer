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
import model.ShoutcastModel;
import viewUser.ShoutcastOutput;

public class ServerCore extends Thread {
	private int port; // port HTTP

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
//					PrintWriter os = new PrintWriter(s.getOutputStream(), true);
//
					String res;
//					
//					res = "ICY 200 OK\r\n"
					res = "HTTP/1.1 200 OK\r\n"
							+ "icy-name: Myradio\r\n"
							+ "icy-genre: Skyrim\r\n"
//							+ "icy-url: http://localhost\r\n"
					
							+ "content-type: audio/mpeg\r\n"
							+ "icy-pub: 1\r\n"
							+ "icy-br: 320\r\n"
//							+ "icy-metaint: 16000\r\n"
							+ "\r\n";
//					
					byte[] b1 = new byte[res.length()];
					for(int i = 0; i < res.length();i++){
						b1[i] = (byte) res.charAt(i);
					}
//					
////	
//					String result;
//					while(!(result = is.readLine()).equals("\r\n"))
					System.out.println(is.readLine());
					System.out.println(is.readLine());
					System.out.println(is.readLine());
					System.out.println(is.readLine());
					System.out.println(is.readLine());
					System.out.println(is.readLine());
					s.getOutputStream().write(b1);
					//apres vérification du header du client on l'ajout /ou non
					HandleClient hc = new HandleClient(s, logger/*, es*/);
					ShoutcastModel.registerClient(s.getInetAddress().getHostAddress(), hc);
					(new Thread(hc)).start();             
//					ShoutcastOutput sco = new ShoutcastOutput(s.getOutputStream()/*, es*/);
					
					/*try {
						File f = new File("a.mp3");
						FileReader fr = new FileReader(f);
						RandomAccessFile media = new RandomAccessFile(f, "r");
						media.seek(129089);
						long end = media.length() - 128;
						byte[] buf = new byte[320000 / 8];
						
						while(media.getFilePointer() < end) {
							media.read(buf);
//							s.getOutputStream().write(es.getData());
							sco.sendData(es.getData());
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
////						char[] tmp = new char[320000 / 8];
////						byte[] tmp2 = new byte[320000 / 8];
////						int size;

////						try {
////							do {
////								size = br.read(tmp);
////								for(int i = 0; i < size ; i++)
////									tmp2[i] = (byte) tmp[i];
////								Thread.sleep(1000);
////								System.out.println("envoie");
////								s.getOutputStream().write(tmp2);
////
////							} while (size > 0);
////							System.out.println("fin envoie");
////						} catch (Exception e) {
////							e.printStackTrace();
////
////						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}*/
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
