package media;

public class Frame {
	
	final static String ALBUM = "TALB";
	final static String ARTIST = "TOPE";
	final static String TITLE = "TIT2";
	
	public static void getFrameData(Metadata metadata, byte[] t) {
		String data, balise;
		int l = 0, m;
		byte[] b;
		while(l < t.length) {
			b = new byte[4];
			for(int i = 0; i < 4; i++) {	//Récupération de la balise
				b[i] = t[l];
				l++;
			}
			balise = Tool.byteToString(b);
			for(int i = 0; i < 4; i++) {	//Récupération de la longueur data
				b[i] = t[l];
				l++;
			}
			m = Tool.charToInt(b[0], b[1], b[2], b[3]);
			b = new byte[m];
			for(int i = 0; i < m; i++) {	//Récupération data
				b[i] = t[l];
				l++;
			}
			data = Tool.byteToString(b);
			if(balise.equals(ALBUM) || balise.equals(ARTIST) || balise.equals(TITLE)) {
				switch(Tool.byteToString(b)) {
				case ALBUM:
					metadata.putData(MetadataType.ALBUM, data);
					break;
				case ARTIST:
					metadata.putData(MetadataType.ARTIST, data);
					break;
				case TITLE:
					metadata.putData(MetadataType.TITLE, data);
					break;
				default:
				}
			}	
		}
	}

}
