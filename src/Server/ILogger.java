package Server;

public interface ILogger {
	public void clientConnected(String ip);
	public void clientDisconnected(String ip);
	public void systemMessage(String msg);
}
