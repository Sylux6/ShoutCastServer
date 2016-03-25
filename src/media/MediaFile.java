package media;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MediaFile {
	
	private String path;
	public File file;
	public Metadata metadata;
	
	// n°byte mp3 data
	private int begin;
	
	private int bitrate;
	private boolean vbr;
	
	public int getBitrate() {
		return bitrate;
	}
	
	public boolean getVBR() {
		return vbr;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public MediaFile(String path) {
		super();
		this.path = path;
		this.file = new File(path);
		this.metadata = new Metadata(this);
		try {
			this.metadata.parse();
			AudioFileFormat audio = AudioSystem.getAudioFileFormat(this.file);
			this.bitrate = (int) audio.getFormat().properties().get("bitrate");
			this.vbr = (boolean) audio.getFormat().properties().get("vbr");
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		System.out.println("BITRATE="+this.bitrate+"\nVBR="+this.vbr);
	}
}
