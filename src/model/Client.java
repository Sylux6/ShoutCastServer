package model;

import java.io.IOException;
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
	public void write(){
		try {
			if(meta)socket.write(fa.getDataWithMeta()); else socket.write(fa.getData());
		} catch (IOException e) {
			ShoutcastModel.unregisterClient(id);
			
		}
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
		if(i < 0) return;
		byte[] metaline = httpreq.substring(i).getBytes();
		i = 13;
		while(metaline[i] != '0' && metaline[i] != '1')
			i++;
		if(metaline[i] != '0')
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
	public String toString(){
		return "("+id+","+ip+")";
	}

}
