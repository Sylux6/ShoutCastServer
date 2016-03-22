package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import model.ShoutcastModel;

public class EmissionSong extends Thread {
	private Playlist list = null;
	public byte[] buf = new byte[320000 / 8];
	public byte[] send;
	int n;
	long start, end, wait;

	public EmissionSong() {
		list = new Playlist();
		MediaFile firstSong = new MediaFile("/home/sylvain/Bureau/test.mp3");
//		MediaFile secondSong = new MediaFile("b.mp3");
//		MediaFile troisSong = new MediaFile("c.mp3");
		list.add(firstSong);
//		list.add(secondSong);
//		list.add(troisSong);
		
	}

	// fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (list.lenght() > 0) {

			MediaFile mf = list.getMedia();
			System.out.println("il reste :"+list.lenght());
			File f = mf.file;
			try {
				RandomAccessFile media = new RandomAccessFile(f, "r");
				media.seek(mf.getBegin());
				mf.metadata.getMetaBuilder().build();
				long end = media.length();
				if(mf.metadata.getID3v1())
					end -= 128; //On s'arrête au niveau de l'ID3v1
				
				// fonction getBitrate?

				while (media.getFilePointer() < end) {
					start = System.currentTimeMillis();
					media.read(buf);
					send = Arrays.copyOf(buf, buf.length + mf.metadata.getMetaBuilder().getMeta().length + 1);
					for(int i = 0; i < mf.metadata.getMetaBuilder().getMeta().length; i++)
						send[buf.length + i + 1] = mf.metadata.getMetaBuilder().getMeta()[i];
					send[buf.length] = (byte) mf.metadata.getMetaBuilder().getN();
					System.out.println("buffer pret");
					end = System.currentTimeMillis();
					System.out.println(end - start + "ms");
					try {
						if((wait = end - start) <= 1000)
							Thread.sleep(1000 - wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ShoutcastModel.notifyBufferChanged(send);
				}
				media.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

	}

	public byte[] getData() {
		return buf;
	}

}
