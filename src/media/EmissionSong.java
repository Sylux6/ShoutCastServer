package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import model.ShoutcastModel;

public class EmissionSong extends Thread {
	public byte[] buf = new byte[320000 / 8];
	private boolean stop = false;

	public EmissionSong() {
		
	}

	// fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (!stop) {
//			System.out.println("on passe ici"+Playlist.lenght());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(Playlist.lenght() > 0){
				MediaFile mf = Playlist.getMedia();
				System.out.println("il reste :"+Playlist.lenght());
				File f = mf.file;
				try {
					FileReader fr = new FileReader(f);
	
					RandomAccessFile media = new RandomAccessFile(f, "r");
					media.seek(mf.getBegin());
					long end = media.length();// - 128;
					// fonction getBitrate?
	
					while (media.getFilePointer() < end) {
						media.read(buf);
						ShoutcastModel.notifyBufferChanged(buf);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
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
