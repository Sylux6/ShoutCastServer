package viewUser;

import java.io.IOException;

public interface ShoutcastProtocol {
	
	public void sendData(byte[] data) throws IOException;
	public void sendError(String msg);
}
