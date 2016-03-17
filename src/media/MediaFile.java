package media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MediaFile {
	
	String path;
	FileInputStream file;
	Metadata metadata;
	
	public MediaFile(String path) {
		super();
		this.path = path;
		try {
			this.file = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
