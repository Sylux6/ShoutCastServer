package events;

import java.io.IOException;


public interface ShoutcastModelEvents {
//	public void userListChanged();
//	public void chatMessageSent(String from, String message);
//	public void privateChatMessageSent(String from, String to, String message);
//	public void shutdownRequested();
	public void bufferReady(byte[] tab) throws IOException;
}
