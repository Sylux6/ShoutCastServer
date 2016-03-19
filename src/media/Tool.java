package media;

public class Tool {

	public static int byteToInt(byte b1, byte b2, byte b3, byte b4) {
		return b1 << 21 | b2 << 14 | b3 << 7 | b4;
	}
	
	public static int byteToInt(byte[] b) throws Exception {
		if(b.length != 4)
			throw new Exception("b doit être de longueur 4");
		return  b[0] << 21 | b[1] << 14 | b[2] << 7 | b[3];
	}
	
	public static String byteToString(byte[] t) {
		String s = "";
		for(int i = 0; i < t.length; i++) {
			s = s + (char)t[i];
		}
		return s;
	}
}
