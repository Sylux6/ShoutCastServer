package controler;

import java.io.IOException;
import java.net.Socket;

import events.ShoutcastModelEvents;
import media.EmissionSong;
import server.ILogger;
import viewUser.ShoutcastOutput;
import viewUser.ShoutcastProtocol;


public class HandleClient implements Runnable, ShoutcastProtocol,ShoutcastModelEvents {
	private final Socket s;
	private ShoutcastOutput cho;
	private String ip = "";
	private ILogger logger = null;

	private enum ClientState {
		ST_INIT, ST_NORMAL
	};

	private ClientState state = ClientState.ST_INIT;
	private boolean stop = false;
	private EmissionSong es;

	public HandleClient(Socket s, ILogger logger/*, EmissionSong es*/) throws IOException {
		this.s = s;
		this.logger = logger;
		/*this.es = es;*/
	}


	@Override
	public void run() {
		try (Socket s1 = s) {
			cho = new ShoutcastOutput(s1.getOutputStream()/*,es*/);
		} catch (IOException ex) {
			if (!stop) {
				System.out.println("on passe par ici?");
//				finish();
			}
		}
	}

	public synchronized void finish() {
		if (!stop) {
			stop = true;
//			try {
//				System.out.println("et la ?");
//				s.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//			if (name != null)
//				ShoutcastModel.unregisterUser(name);
//			logger.clientDisconnected(s.toString(), name);
		}
	}

	@Override
	public void sendError(String msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void sendData(byte[] data) {
		// TODO Auto-generated method stub
		cho.sendData(data);
	}


	@Override
	public void bufferReady(byte[] tab) {
		// TODO Auto-generated method stub
		sendData(tab);
	}
}
