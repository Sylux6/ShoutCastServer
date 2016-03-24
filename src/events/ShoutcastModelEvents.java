package events;

import java.io.IOException;


public interface ShoutcastModelEvents {
	public void bufferReady(byte[] tab) throws IOException;
}
