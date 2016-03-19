package media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

public class Metadata {
	private int vID;
	byte[] metadata;
	MediaFile mediafile;
	private String artist = null;
	private String title = null;
	private String album = null;
	
	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getAlbum() {
		return album;
	}
	
	public Metadata(MediaFile mediafile) {
		this.mediafile = mediafile;
	}
	
	public boolean parse() throws IOException {
        RandomAccessFile file = new RandomAccessFile(mediafile.file, "r");	//Mode lecture
        byte[] header = new byte[10];
        file.read(header);
 
        if (header[0] != 'I' || header[1] != 'D' || header[2] != '3')
            return parseID3v1(file);
            
        vID = header[3];
        if (vID < 0 || vID > 4)
        	return parseID3v1(file);
        
        int metadatasize = Tool.byteToInt(header[6], header[7], header[8], header[9]) + 10;
        mediafile.setBegin(metadatasize);
        
        boolean uses_synch = (header[5] & 0x80) != 0 ? true : false;
        boolean has_extended_hdr = (header[5] & 0x40) != 0 ? true : false;
         
        // Read the extended header length and skip it
        if (has_extended_hdr) {
            int headersize = file.read() << 21 | file.read() << 14 | file.read() << 7 | file.read();
            file.skipBytes(headersize - 4);
        }
         
        byte[] buffer = new byte[metadatasize];
        file.read(buffer);
        file.close();
        metadata = buffer;
 
        // Prepare to parse the tag
        int length = buffer.length;
         
        // Recreate the tag if desynchronization is used inside; we need to replace 0xFF 0x00 with 0xFF
        if (uses_synch) {
            int newpos = 0;
            byte[] newbuffer = new byte[metadatasize];
             
            for (int i = 0; i < buffer.length; i++) {
                if (i < buffer.length - 1 && (buffer[i] & 0xFF) == 0xFF && buffer[i+1] == 0) {
                    newbuffer[newpos++] = (byte) 0xFF;
                    i++;
                    continue;
                }
                 
                newbuffer[newpos++] = buffer[i];
            }
 
            length = newpos;
            buffer = newbuffer;
        }
 
        // Set some params
        int pos = 0;
        final int ID3FrameSize = vID < 3 ? 6 : 10;
         
        // Parse the tags
        while (true) {
            int rembytes = length - pos;
                 
            // Do we have the frame header?
            if (rembytes < ID3FrameSize)
                break;
             
            // Is there a frame?
            if (buffer[pos] < 'A' || buffer[pos] > 'Z')
                break;
             
            // Frame name is 3 chars in pre-ID3v3 and 4 chars after
            String framename;
            int framesize;
             
            if (vID < 3) {
                framename = new String(buffer, pos, 3);
                framesize = ((buffer[pos+5] & 0xFF) << 8 ) | ((buffer[pos+4] & 0xFF) << 16 ) | ((buffer[pos+3] & 0xFF) << 24 );
            }
            else {
                framename = new String(buffer, pos, 4);
                framesize = (buffer[pos+7] & 0xFF) | ((buffer[pos+6] & 0xFF) << 8 ) | ((buffer[pos+5] & 0xFF) << 16 ) | ((buffer[pos+4] & 0xFF) << 24 );
            }
             
            if(pos + framesize > length)
                break;
             
            if (framename.equals("TPE1") || framename.equals("TPE2") || framename.equals("TPE3") || framename.equals("TPE")) {
                if(artist == null)
                	artist = parseTextField(buffer, pos + ID3FrameSize, framesize);
            }
                   
            if(framename.equals("TIT2") || framename.equals("TIT")) {
            	if(title == null)
            		title = parseTextField(buffer, pos + ID3FrameSize, framesize);
            }
            
            if(framename.equals("TALB")) {
            	if(album == null)
            		album = parseTextField(buffer, pos + ID3FrameSize, framesize);
            }
                     
            pos += framesize + ID3FrameSize;
            continue;
        }
         
        return title != null || artist != null || album != null;
    }
 
    private String parseTextField(final byte[] buffer, int pos, int size) {
        if (size < 2)
            return null;
        Charset charset;
        int charcode = buffer[pos]; 
        if (charcode == 0)
            charset = Charset.forName("ISO-8859-1");
        else if (charcode == 3)
            charset = Charset.forName("UTF-8");
        else
            charset = Charset.forName("UTF-16");
             
        return charset.decode(ByteBuffer.wrap(buffer, pos + 1, size - 1)).toString();
    }
    
    public boolean parseID3v1(RandomAccessFile file) throws IOException {
    	mediafile.setBegin(0);	//Absence de tag ID3v2
    	file.seek(file.length()-128);
        byte[] buf = new byte[128];
        file.read(buf);
        file.close();
        if(buf[0] != 'T' || buf[1] != 'A' || buf[2] != 'G') {
        	
        }
        metadata = buf;
        byte[] title = new byte[30];
        byte[] artist = new byte[30];
        byte[] album = new byte[30];
        for(int i = 0; i < 90; i++) {
        	if(i < 30)
        		title[i] = buf[i+3];
        	else if(i < 60)
        		artist[i-30] = buf[i+3];
        	else
        		album[i-60] = buf[i+3];
        }
        this.title = Tool.byteToString(title);
        this.album = Tool.byteToString(album);
        this.artist = Tool.byteToString(artist);
        return true;
    }

}
