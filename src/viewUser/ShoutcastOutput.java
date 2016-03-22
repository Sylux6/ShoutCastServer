package viewUser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import media.EmissionSong;
import model.ShoutcastModel;


//flux d'emission , comsommateur du buffeur (chaque client a le sien)   
public class ShoutcastOutput implements ShoutcastProtocol{
	PrintWriter os;
	OutputStream out;
	EmissionSong es;
	public ShoutcastOutput(OutputStream out/*,EmissionSong es*/) throws IOException {
		this.os = new PrintWriter(out, true);
		this.out = out;
		/*this.es = es;*/
	}
	
	@Override
	public synchronized void sendError(String msg) {
		os.println(msg);
	}

	@Override
	public void sendData(byte[] data) throws IOException {
		// TODO Auto-generated method stub
			out.write(data);

		
	}

	
}
