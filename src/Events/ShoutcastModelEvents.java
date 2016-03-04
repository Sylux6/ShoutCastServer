package Events;

public interface ShoutcastModelEvents extends ShoutcastEvents, RoomEvents {
	public void userListChanged();
	public void chatMessageSent(String from, String message);
	public void privateChatMessageSent(String from, String to, String message);
	public void shutdownRequested();
}
