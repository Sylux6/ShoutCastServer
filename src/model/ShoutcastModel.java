package model;

import java.util.concurrent.ConcurrentHashMap;

import server.ILogger;
import server.TextLogger;

public class ShoutcastModel {
	public static final ConcurrentHashMap<Integer, Client> clientList = new ConcurrentHashMap<>();
	private static ILogger tl = new TextLogger();
	public static synchronized void registerClient(Client c) {
		clientList.put(c.getId(), c);
		tl.clientConnected(c.toString());
	}

	public static synchronized void unregisterClient(int id) {
		tl.clientDisconnected(clientList.get(id).toString());
		clientList.remove(id);
		
	}

	public static void clearAll() {
		// TODO?
	}

	public static void notifyBufferChanged() {
		for (Client c : clientList.values()) {
			c.write();
		}
	}
}
