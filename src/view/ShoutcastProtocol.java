package view;

import java.util.Collection;

public interface ShoutcastProtocol {
	
	public void sendQuit();
	public void sendError(String msg);
}
