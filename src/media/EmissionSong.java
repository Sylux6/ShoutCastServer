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
	public byte[] meta;
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
			if(mf.metadata.getMetadata().length % 4 != 0)
				n = ((mf.metadata.getMetadata().length / 4) + (mf.metadata.getMetadata().length % 4) * 4);
			else
				n = (mf.metadata.getMetadata().length / 4);
			meta = new byte[n * 4];
			meta[0] = (byte)n;
			for(int i = 0; i < mf.metadata.getMetadata().length; i++)
				meta[i+1] = mf.metadata.getMetadata()[i];
			System.out.println("il reste :"+list.lenght());
			File f = mf.file;
			try {
				RandomAccessFile media = new RandomAccessFile(f, "r");
				media.seek(mf.getBegin());
				long end = media.length();// - 128;
				// fonction getBitrate?

				while (media.getFilePointer() < end) {
					start = System.currentTimeMillis();
					media.read(buf);
					send = Arrays.copyOf(buf, buf.length + meta.length);
					for(int i = 0; i < meta.length; i++)
						send[buf.length+i] = meta[i];
					System.out.println("buffer pret");
					ShoutcastModel.notifyBufferChanged(send);
					end = System.currentTimeMillis();
					System.out.println(end - start + "ms");
					try {
						if((wait = end - start) <= 1000)
							Thread.sleep(1000 - wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
