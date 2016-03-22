package media;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MetaBuilder {
	private byte[] meta;
	private int n;
	private Metadata metadata;
	
	public MetaBuilder(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public void build() {
		String tmp = "StreamTitle='" + metadata.getArtist() + " - " + metadata.getTitle() + "';";
		byte[] metatmp = tmp.getBytes(StandardCharsets.UTF_8);
		int mod = metatmp.length % 16;
		if(mod == 0)
			n = metatmp.length / 16;
		else
			n = (metatmp.length + (16 - mod)) / 16;
		meta = Arrays.copyOf(metatmp, metatmp.length + (16 - mod));
	}
	
	public byte[] getMeta() {
		return meta;
	}
	
	public int getN() {
		return n;
	}
	
}
