package model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import events.ShoutcastModelEvents;

public class ShoutcastModel {
	public static final TreeMap<String, OutputStream> clientList = new TreeMap<>();

	public static synchronized void registerClient(String ip, OutputStream socket) {
		System.out.println("on add le client :"+ip);
		clientList.put(ip, socket);
	}

	public static synchronized void unregisterClient(String ip) {
		clientList.remove(ip);
	}

	public static synchronized Set<String> getClientIP() {
		return clientList.keySet();
	}

	public static void clearAll() {
		//TODO?
	}
	public static void notifyBufferChanged(byte[] buf){
		for(OutputStream s: clientList.values()){
			try {
				s.write(buf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void printAllClient() {
		System.out.println("->");
		for (String s : getClientIP()) {
			System.out.println(s);
		}
		System.out.println("fin");
	}
}
