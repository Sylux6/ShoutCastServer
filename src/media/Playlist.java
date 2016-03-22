package media;

import java.util.ArrayList;

import events.Observateur;

public class Playlist {
	public static ArrayList<MediaFile> pl = new ArrayList<>();
	private static boolean loop = false;
	private static ArrayList<Observateur> obs = new ArrayList<>();
	private static String current;

	public Playlist() {

	}

	public static MediaFile getMedia() {
		
		MediaFile ret = pl.get(0);
		current = pl.get(0).getPath();
		if (loop) {
			pl.add(ret);
		}
		pl.remove(0);
		notifyPlaylistChanged();
		return ret;
	}

	public static void swap(int elt, boolean up) {
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

	public static void add(String s) {
		MediaFile mf = new MediaFile(s);
		pl.add(mf);
		notifyPlaylistChanged();
	}

	public static void remove(int elt) {
		pl.remove(elt);
		notifyPlaylistChanged();
	}

	public static void clear() {
		pl = new ArrayList<>();
		notifyPlaylistChanged();
	}

	public static int lenght() {
		return pl.size();
	}

	public static void enableloop() {
		loop = true;
	}

	public static void disableLoop() {
		loop = false;
	}

	public static String toString_() {
		StringBuilder s = new StringBuilder();
		for (MediaFile mf : pl) {
			s.append(mf.getPath() + "\n");
		}
		return s.toString();
	}

	
	private static void notifyPlaylistChanged() {
		for (Observateur ob : obs) {
			ob.getPlaylist();
		}
	}

	public static void addObs(Observateur ob) {
		obs.add(ob);
	}
	public static String getCurrent(){
		return current;
	}

}
