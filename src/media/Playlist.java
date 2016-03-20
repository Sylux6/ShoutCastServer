package media;

import java.util.ArrayList;

public class Playlist {
	private ArrayList<MediaFile> pl = new ArrayList<>();
	
	public Playlist(){
	}
	public Playlist(MediaFile mf){
		pl.add(mf);
	}
	public Playlist(ArrayList<MediaFile> mfList){
		
		pl.addAll(mfList);
	}
	
	
	public MediaFile getMedia(){
		MediaFile ret = pl.get(0);
		pl.remove(0);
		return ret;
	}
	
	public void add(MediaFile mf){
		pl.add(mf);
	}
	public void add(ArrayList<MediaFile> mfList){
		pl.addAll(mfList);
	}
	public void clear(){
		pl = new ArrayList<>();
	}
	
	//ajout de nouvelle fonctionnalité?
	
}
