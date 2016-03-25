package media;

import java.util.ArrayList;

import events.Observateur;

public class Playlist {
	public  ArrayList<MediaFile> pl = new ArrayList<>();
	private boolean loop = false;
	private ArrayList<Observateur> obs = new ArrayList<>();
	private String current;

	public Playlist() {


	}

	public MediaFile getMedia() {
		
		MediaFile ret = pl.get(0);
		current = pl.get(0).getPath();
		if (loop) {
			pl.add(ret);
		}
		pl.remove(0);
		notifyPlaylistChanged();
		return ret;
	}

	public void swap(int elt, boolean up) {
		if(elt-1 < 0 || elt+1 > pl.size()){
			return;
		}
		
		MediaFile tmp = pl.get(elt);
		if (up) {
			pl.set(elt, pl.get(elt - 1));
			pl.set(elt - 1, tmp);
		} else {
			pl.set(elt, pl.get(elt + 1));
			pl.set(elt + 1, tmp);
		}
		notifyPlaylistChanged();
	}

	public  void add(String s) {
		MediaFile mf = new MediaFile(s);
		pl.add(mf);
		notifyPlaylistChanged();
	}

	public  void remove(int elt) {
		if(elt <0 || elt > pl.size()){
			return;
		}
		pl.remove(elt);
		notifyPlaylistChanged();
	}

	public  void clear() {
		pl = new ArrayList<>();
		notifyPlaylistChanged();
	}

	public  int lenght() {
		return pl.size();
	}

	public  void enableloop() {
		loop = true;
	}

	public  void disableLoop() {
		loop = false;
	}

	
	public  String toString() {
		StringBuilder s = new StringBuilder();
		for (MediaFile mf : pl) {
			s.append(mf.getPath() + "\r\n");
		}
		return s.toString();
	}

	
	private  void notifyPlaylistChanged() {
		for (Observateur ob : obs) {
			ob.getPlaylist();
		}
	}

	public  void addObs(Observateur ob) {
		obs.add(ob);
	}
	public  String getCurrent(){
		return current;
	}

}
