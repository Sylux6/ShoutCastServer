package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class EmissionSong extends Thread{
	private Playlist  list = null;
	public byte[] buf = new byte[320000 / 8];
	
	public EmissionSong(){
		list = new Playlist();
		MediaFile firstSong = new MediaFile("a.mp3");
		list.add(firstSong);
		list.add(firstSong);
	}
	
	
	//fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			MediaFile mf = list.getMedia();
			File f =  mf.file;
			try {
				FileReader fr = new FileReader(f);
			
			RandomAccessFile media = new RandomAccessFile(f, "r");
			media.seek(mf.getBegin());
			long end = media.length() ;//- 128;
			 //fonction getBitrate?
			
			while(media.getFilePointer() < end) {
				media.read(buf);
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
