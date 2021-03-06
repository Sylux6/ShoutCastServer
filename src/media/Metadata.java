package media;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Metadata {
	private int vID;
	private byte[] metadata;
	MediaFile mediafile;
	private String artist = null;
	private String title = null;
	private MetaBuilder builder;
	private boolean id3v1 = true;
	
	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}
	
	public Metadata(MediaFile mediafile) {
		this.mediafile = mediafile;
		this.builder = new MetaBuilder(this);
		RandomAccessFile file;
		try {
			file = new RandomAccessFile(mediafile.file, "r");
			file.seek(file.length()-128);
	        byte[] buf = new byte[3];
	        file.read(buf);
	        file.close();
	        if(buf[0] != 'T' || buf[1] != 'A' || buf[2] != 'G')
	        	this.id3v1 = false;
	        else
	        	this.id3v1 = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getID3v1() {
		return this.id3v1;
	}
	
	public MetaBuilder getMetaBuilder() {
		return builder;
	}
	
	public byte[] getMetadata() {
		return this.metadata;
	}
	
	public boolean parse() throws IOException {
        RandomAccessFile file = new RandomAccessFile(mediafile.file, "r");	//Mode lecture
        byte[] header = new byte[10];
        file.read(header);
 
        if (header[0] != 'I' || header[1] != 'D' || header[2] != '3') {
        	mediafile.setBegin(0);	// Absence de tag ID3v2
        	file.close();
            return parseID3v1();
        }
            
        vID = header[3];
        if (vID < 0 || vID > 4) {
        	mediafile.setBegin(0);	// version ID3v2 non reconnue
        	file.close();
        	return parseID3v1();
        }
        
        int metadatasize = Tool.byteToInt(header[6], header[7], header[8], header[9]) + 10;
        mediafile.setBegin(metadatasize);
        
        boolean uses_synch = (header[5] & 0x80) != 0 ? true : false;
        boolean extended_header = (header[5] & 0x40) != 0 ? true : false;
         
        // On ignore le header étendu si nécessaire
        if (extended_header) {
            int headersize = file.read() << 21 | file.read() << 14 | file.read() << 7 | file.read();
            file.skipBytes(headersize - 4);
        }
         
        byte[] buffer = new byte[metadatasize];
        file.read(buffer);
        file.close();
        metadata = buffer;
        int length = buffer.length;
         
        // Modification du tag ID3v2 si il y a désynchronisation
        if (uses_synch) {
            int newpos = 0;
            byte[] tmp = new byte[metadatasize];
             
            for (int i = 0; i < buffer.length; i++) {
                if (i < buffer.length - 1 && (buffer[i] & 0xFF) == 0xFF && buffer[i+1] == 0) {
                    tmp[newpos++] = (byte) 0xFF;
                    i++;
                    continue;
                }
                 
                tmp[newpos++] = buffer[i];
            }
 
            length = newpos;
            buffer = tmp;
        }
 
        int pos = 0;
        final int ID3FrameSize = vID < 3 ? 6 : 10;
         
        // Parsing
        while (true) {
            int leftovers = length - pos;
            
            if (leftovers < ID3FrameSize)
                break;
             
            if (buffer[pos] < 'A' || buffer[pos] > 'Z')
                break;
             
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
                     
            pos += framesize + ID3FrameSize;
            continue;
        }
         
        return title != null || artist != null;
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
    
    public boolean parseID3v1() throws IOException {
    	RandomAccessFile file = new RandomAccessFile(mediafile.file, "r");
    	file.seek(file.length()-128);
        byte[] buf = new byte[128];
        file.read(buf);
        file.close();
        if(buf[0] != 'T' || buf[1] != 'A' || buf[2] != 'G') {
        	return false;
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
        this.artist = Tool.byteToString(artist);
        return true;
    }

}
