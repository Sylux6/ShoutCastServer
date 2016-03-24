package server;

public interface ILogger {
	public void clientConnected(String id);
	public void clientDisconnected(String id);
	public void systemMessage(String musqiue);
	public void currentMusic(String musique);
}
