package model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import events.ShoutcastModelEvents;

public class ShoutcastModel {
	public static final TreeMap<Integer, Client> clientList = new TreeMap<>();

	public static synchronized void registerClient(Client c) {
		System.out.println("client added: id=" + c.getId() + " IP=" + c.getIp());
		clientList.put(c.getId(), c);
	}

	public static synchronized void unregisterClient(int id) {
		clientList.remove(id);
	}

//	public static synchronized Set<String> getClientIP() {
//		return clientList.keySet();
//	}

	public static void clearAll() {
		//TODO?
	}
	public static void notifyBufferChanged(){
		for(Client c: clientList.values()){
			try {
				if(c.isMeta())
					c.getSocket().write(c.getFlux().getDataWithMeta());
				else
					c.getSocket().write(c.getFlux().getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				unregisterClient(c.getId());
			}
		}
	}

//	public static void printAllClient() {
//		System.out.println("->");
//		for (String s : getClientIP()) {
//			System.out.println(s);
//		}
//		System.out.println("fin");
//	}
}
