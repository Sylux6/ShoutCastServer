package media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

import model.ShoutcastModel;

public class FluxAudio extends Thread {
	public byte[] buf = new byte[320000 / 8];	//40000
	private boolean stop = false;
	private boolean next = false;
	private Playlist pl;

	public byte[] send;
	int n, bitrate;
	long start, end, wait;

	public FluxAudio() {
		this.pl = new Playlist();
	}

	public FluxAudio(Playlist pl) {
		this.pl = pl;
	}

	// fonction de modification de la playList en cours
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (!stop) {
			// System.out.println("pl : " + pl.lenght());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (pl.lenght() > 0) {
				next = false;
				MediaFile mf = pl.getMedia();
				bitrate = mf.getBitrate();

				File f = mf.file;

				RandomAccessFile media;
				try {
					// Remplacement du buffer pour partir sur une base propre
					buf = new byte[40000];
					media = new RandomAccessFile(f, "r");
					System.out.println(mf.getBitrate());
					System.out.println("BEGIN:"+mf.getBegin());
					media.seek(mf.getBegin());
					mf.metadata.getMetaBuilder().build();
					long endmp3 = media.length();
					long end;
					
					// On s'arrête au niveau de l'ID3v1 lors de la lecture
					if (mf.metadata.getID3v1())
						endmp3 -= 128;

					while (media.getFilePointer() < endmp3 && !next) {
						
						start = System.currentTimeMillis();
						setData(mf, media, bitrate);
						setDataWithMeta(mf, media, bitrate);
						ShoutcastModel.notifyBufferChanged();
						end = System.currentTimeMillis();
						try {
							if ((wait = end - start) <= 1000)
								Thread.sleep(1000 - wait);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
					next = false;
					media.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	public void enableNext() {
		next = true;
	}


	public synchronized void setData(MediaFile mf, RandomAccessFile media, int bitrate)  {

		try {
			// Bitrate correct
			if(bitrate == 320000)
				media.read(buf);
			
			// Bitrate trop faible
			else if(bitrate < 320000) {
				// Nombre d'octets à compléter
				int reste = (320000 - bitrate) / 8;
				// Tous les div octets on répète l'octet précédent
				int div = 40000 / reste;
				int j = 0;
				byte[] tmp = new byte[div];
				for(int i = 0; i < 40000; i = i + div) {
					if(j == div) {
						buf[i] = buf[i-1];
						j = 0;
					}
					else {
						media.read(tmp);
						for(int k = 0; k < div; k++) {
							buf[i + k] = tmp[k];
						}
					}
				}
					j++;
			}
			
			// Bitrate trop élevé
			else {
				// Combien d'octets en trop ?
				int exces = (bitrate - 320000) / 8;
				// On saute tous les div octets à la lecture
				int coupe = bitrate / 8;
				for(int i = 0; i < exces; i++)
					coupe /= 2;
				byte[] tmp = new byte[coupe];
				for(int i = 0; i < 40000; i = i + coupe) {
					media.read(tmp);
					for(int j = 0; j < coupe; j++)
						buf[i] = tmp[j];
					media.skipBytes(1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void setDataWithMeta(MediaFile mf, RandomAccessFile media, int bitrate) {
//			send = Arrays.copyOf(buf, newLength)
			send = insertMeta(buf, mf.metadata.getMetaBuilder().getN(),mf.metadata.getMetaBuilder().getMeta(), 40000);
	}
	
	private byte[] insertMeta(byte[] a1,int nb,byte[] a2, int pos){
		send = new byte[a1.length+a2.length+1];
		for(int i = 0 ; i < pos; i++){
			send[i] = a1[i]; 
		}
		send[pos] = (byte)nb;
		for(int i = 0 ; i < a2.length; i++){
			send[pos+i+1] = a2[i];
		}
		for(int i = 0 ; i < a1.length-pos; i++){
			send[a2.length+pos+i+1] = a1[pos+i];
		}
		
		return send;
	}

	public synchronized byte[] getData() {
		// renvoyer que un bout si le bitrate est petit
		return buf;
	}

	public synchronized byte[] getDataWithMeta() {
		// construction ici serai mieux
		return send;
	}


	public Playlist getPlaylist() {
		return pl;
	}

}
