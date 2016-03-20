package viewUser;

public interface ShoutcastProtocol {
	
	public void sendData(byte[] data);
	public void sendError(String msg);
}
