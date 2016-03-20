package media;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Metadata {
	private Map<MetadataType, String> data = new TreeMap<MetadataType, String>();
	int vID;
	
	public Metadata() {
		
	}
	
	public Metadata (String artist, String title, String album) {
		this.data.put(MetadataType.ARTIST, artist);
		this.data.put(MetadataType.TITLE, title);
		this.data.put(MetadataType.ALBUM, album);
	}
	
	public String get(String metadatatype) {
		return data.get(metadatatype);
	}
	
	public void putData(MetadataType t, String s) {
		this.data.put(t, s);
	}
	
	public void set(MediaFile media) throws Exception {
		int count = 0, taille;
		byte buf[] = new byte[10];
		count += media.file.read(buf, 0, 3);
		if(buf[0] == 'I' && buf[1] == 'D' && buf[0] == '3')
			vID = 2;
		else
			vID = 1;
		//Version v2 de ID3, metadata en tête de fichier, en fin de fichier sinon (v1)
		if(vID == 2) {
			count += media.file.read(buf, count, 3);
			count += media.file.read(buf, count, 4);
			//Conversion de 4 valeurs char vers un int
			taille = Tool.charToInt(buf[0], buf[1], buf[2], buf[3]);
			byte[] tmp = new byte[taille];
			count += media.file.read(tmp, count, taille);
			
		}
	}

}
