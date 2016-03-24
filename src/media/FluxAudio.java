package media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

import model.ShoutcastModel;

public class FluxAudio extends Thread {
	public byte[] buf = new byte[320000 / 8];
	private boolean stop = false;
	private boolean next = false;

	private boolean pause = false;
	private Playlist pl;

	public byte[] send;
	int n;
	long start, end, wait;
	public FluxAudio() {
		this.pl = new Playlist();
	}
	public FluxAudio(Playlist pl){
		this.pl = pl;
	}

	// fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (!stop) {
			// System.out.println("on passe ici"+Playlist.lenght());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			if (pl.lenght() > 0) {
				next = false;
				MediaFile mf = pl.getMedia();
				System.out.println("il reste :" + pl.lenght());
				File f = mf.file;

				RandomAccessFile media;
				try {
					media = new RandomAccessFile(f, "r");

					media.seek(mf.getBegin());
					mf.metadata.getMetaBuilder().build();
					long endmp3 = media.length();
					long end;
					if (mf.metadata.getID3v1())
						endmp3 -= 128; // On s'arrête au niveau de l'ID3v1

					// fonction getBitrate?
					System.out.println("->" + next);
					while (media.getFilePointer() < endmp3 && !next) {
						start = System.currentTimeMillis();
						int lu;
						synchronized (buf) {
							lu = media.read(buf);
						}

						if (lu < buf.length) {
							for (int i = 0; i < buf.length - lu; i++) {
								buf[lu + i] = 0;
							}
						}
						send = Arrays.copyOf(buf, buf.length + mf.metadata.getMetaBuilder().getMeta().length + 1);
						synchronized (send) {
							for (int i = 0; i < mf.metadata.getMetaBuilder().getMeta().length; i++)
								send[buf.length + i + 1] = mf.metadata.getMetaBuilder().getMeta()[i];
							send[buf.length] = (byte) mf.metadata.getMetaBuilder().getN();
						}

						System.out.println("buffer pret");
						end = System.currentTimeMillis();
						System.out.println(end - start + "ms");
						try {
							if ((wait = end - start) <= 1000)
								Thread.sleep(1000 - wait);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ShoutcastModel.notifyBufferChanged();
					}
					next = false;
					media.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}

	public void enableNext() {
		System.out.println("->on active next");
		next = true;
	}

	public synchronized void setData(byte[] buf) {
		this.buf = buf;
	}

	public synchronized void setDataWithMeta(byte[] buf) {
		this.send = buf;
	}

	public synchronized byte[] getData() {
		//renvoyer que un bout si le bitrate est petit
		return buf;
	}

	public synchronized byte[] getDataWithMeta() {
		//construction ici serai mieux
		return send;
	}
	
	
	public void enablePause(){
		pause = true;
	}
	public void disablePause(){
		pause = false;
	}
	public Playlist getPlaylist(){
		return pl;
	}

}
