package media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MediaFile {
	
	private String path;
	public File file;
	public Metadata metadata;
	private int begin;	//n°byte mp3 data
	
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
	}
}
