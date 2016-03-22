package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import model.ShoutcastModel;

public class EmissionSong extends Thread {
	public byte[] buf = new byte[320000 / 8];
	private boolean stop = false;

	public byte[] send;
	int n;
	long start, end, wait;

	public EmissionSong() {
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
			if (Playlist.lenght() > 0) {
				MediaFile mf = Playlist.getMedia();
				System.out.println("il reste :" + Playlist.lenght());
				File f = mf.file;

				RandomAccessFile media;
				try {
					media = new RandomAccessFile(f, "r");

					media.seek(mf.getBegin());
					mf.metadata.getMetaBuilder().build();
					long end = media.length();
					if (mf.metadata.getID3v1())
						end -= 128; // On s'arrête au niveau de l'ID3v1

					// fonction getBitrate?

					while (media.getFilePointer() < end) {
						start = System.currentTimeMillis();
						media.read(buf);
						send = Arrays.copyOf(buf, buf.length + mf.metadata.getMetaBuilder().getMeta().length + 1);
						for (int i = 0; i < mf.metadata.getMetaBuilder().getMeta().length; i++)
							send[buf.length + i + 1] = mf.metadata.getMetaBuilder().getMeta()[i];
						send[buf.length] = (byte) mf.metadata.getMetaBuilder().getN();
						System.out.println("buffer pret");
						end = System.currentTimeMillis();
						System.out.println(end - start + "ms");
						try {
							if ((wait = end - start) <= 1000)
								Thread.sleep(1000 - wait);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ShoutcastModel.notifyBufferChanged(send, buf);
					}
					System.out.println("closed");
					media.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	public byte[] getData() {
		return buf;
	}

}
