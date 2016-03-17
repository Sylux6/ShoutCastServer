package media;

public class Tool {

	public static int charToInt(byte c1, byte c2, byte c3, byte c4) {
		return c1 << 24 | c2 << 16 | c3 << 8 | c4;
	}
	
	public static String byteToString(byte[] t) {
		String s = "";
		for(int i = 0; i < t.length; i++) {
			s = s + (char)t[i];
		}
		return s;
	}
}
