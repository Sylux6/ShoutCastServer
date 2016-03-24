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

	public FluxAudio(Playlist pl) {
		this.pl = pl;
	}

	// fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (!stop) {
			// System.out.println("pl : " + pl.lenght());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (pl.lenght() > 0) {
				next = false;
				MediaFile mf = pl.getMedia();
				buf = new byte[mf.getBitrate()/8];
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

					while (media.getFilePointer() < endmp3 && !next) {
						start = System.currentTimeMillis();
						setData(mf, media);
						setDataWithMeta(mf, media);
						ShoutcastModel.notifyBufferChanged();
						end = System.currentTimeMillis();
						try {
							if ((wait = end - start) <= 1000)
								Thread.sleep(1000 - wait);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}

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
		next = true;
	}

	public synchronized void setData(MediaFile mf, RandomAccessFile media)  {
		try {
			media.read(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void setDataWithMeta(MediaFile mf, RandomAccessFile media) {
//			send = Arrays.copyOf(buf, newLength)
			send = insertMeta(buf, mf.metadata.getMetaBuilder().getN(),mf.metadata.getMetaBuilder().getMeta(), 40000);
	}
	
	private byte[] insertMeta(byte[] a1,int nb,byte[] a2, int pos){
		send = new byte[a1.length+a2.length+1];
		for(int i = 0 ; i < pos; i++){
			send[i] = a1[i]; 
		}
		send[pos] = (byte)nb;
		for(int i = 0 ; i < a2.length; i++){
			send[pos+i+1] = a2[i];
		}
		for(int i = 0 ; i < a1.length-pos; i++){
			send[a2.length+pos+i+1] = a1[pos+i];
		}
		
		return send;
	}

	public synchronized byte[] getData() {
		// renvoyer que un bout si le bitrate est petit
		return buf;
	}

	public synchronized byte[] getDataWithMeta() {
		// construction ici serai mieux
		return send;
	}

	public void enablePause() {
		pause = true;
	}

	public void disablePause() {
		pause = false;
	}

	public Playlist getPlaylist() {
		return pl;
	}

}
