package controler;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

import events.ShoutcastModelEvents;
import model.ShoutcastModel;
import server.ILogger;
import viewUser.ShoutcastInput;
import viewUser.ShoutcastOutput;
import viewUser.ShoutcastProtocol;
import viewUser.ShoutcastProtocolException;

public class HandleClient implements Runnable, ShoutcastProtocol {
	private final Socket s;
	private ShoutcastOutput cho;
	private ShoutcastInput chi;
	private String ip = "";
	private ILogger logger = null;

	private enum ClientState {
		ST_INIT, ST_NORMAL
	};

	private ClientState state = ClientState.ST_INIT;
	private boolean stop = false;

	public HandleClient(Socket s, ILogger logger) throws IOException {
		this.s = s;
		this.logger = logger;
	}

	@Override
	public void sendQuit() {
		ShoutcastModel.unregisterUser(this.name);
		clientListChanged();
		logger.clientDisconnected(s.toString());
	}

	@Override
	public void run() {
		try (Socket s1 = s) {
			cho = new ShoutcastOutput(s1.getOutputStream());
			chi = new ShoutcastInput(s1.getInputStream(), this);
			try {
				chi.doRun();
			} catch (ShoutcastProtocolException e) {
				finish();
			}
		} catch (IOException ex) {
			if (!stop) {
				finish();
			}
		}
	}

	public synchronized void finish() {
		if (!stop) {
			stop = true;
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if (name != null)
				ShoutcastModel.unregisterUser(name);
			logger.clientDisconnected(s.toString(), name);
		}
	}

	@Override
	public void sendError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
