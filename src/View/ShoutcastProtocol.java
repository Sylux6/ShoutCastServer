package View;

import java.util.Collection;

public interface ShoutcastProtocol {
	
	public void sendQuit();
	public void sendError(String msg);
}
