package model;

import java.io.OutputStream;

import media.FluxAudio;

public class Client {
	private String ip;
	private int id;
	private boolean meta = false;
	private String httpreq;
	private OutputStream socket;
	private FluxAudio fa;
	
	public OutputStream getSocket() {
		return socket;
	}

	public Client(String ip, String httpreq, int id, OutputStream s,FluxAudio fa) {
		super();
		this.ip = ip;
		this.httpreq = httpreq;
		this.id = id;
		this.socket = s;
		this.fa = fa;
		parse();
	}
	
	private void parse() {
		int i = httpreq.indexOf("Icy-MetaData");
		if(i < 0)
			return;
		byte[] metaline = httpreq.substring(i).getBytes();
		if(metaline[14] != 0)
			meta = true;
	}
	
	public String getIp() {
		return ip;
	}
	public int getId() {
		return id;
	}
	public boolean isMeta() {
		return meta;
	}
	public String getHttpreq() {
		return httpreq;
	}
	public FluxAudio getFlux(){
		return fa;
	}

}
