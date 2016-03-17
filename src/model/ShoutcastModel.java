package model;

import java.util.Set;
import java.util.TreeMap;

import events.ShoutcastModelEvents;

public class ShoutcastModel {
	public static final TreeMap<String, ShoutcastModelEvents> clientList = new TreeMap<>();

	public static synchronized void registerClient(String ip, ShoutcastModelEvents client) {
		clientList.put(ip, client);
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

	public static void printAllClient() {
		System.out.println("->");
		for (String s : getClientIP()) {
			System.out.println(s);
		}
		System.out.println("fin");
	}
}
