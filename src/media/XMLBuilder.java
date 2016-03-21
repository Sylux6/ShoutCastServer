package media;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class XMLBuilder {
	private byte[] meta;
	private int n;
	private Metadata metadata;
	
	public XMLBuilder(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public void build() {
		String tmp =	"<?xml \"version=1.0\" encoding=\"UTF-8\" ?>\n"
					+	"<metadata>\n"
					+	"<TALB>TEST ALBUM</TALB>\n"
					+	"<TIT2>TEST TITLE</TIT2>\n"
					+	"<TPE1>TEST ARTIST</TPE1>\n"
					+	"</metadata>";
		byte[] metatmp = tmp.getBytes(StandardCharsets.UTF_8);
		int mod = metatmp.length % 16;
		if(mod == 0)
			n = metatmp.length / 16;
		else
			n = (metatmp.length + (16 - mod)) / 16;
		meta = Arrays.copyOf(metatmp, n);
	}
	
	public byte[] getMeta() {
		return meta;
	}
	
	public int getN() {
		return n;
	}
	
}
