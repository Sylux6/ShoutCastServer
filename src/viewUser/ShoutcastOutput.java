package viewUser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import model.ShoutcastModel;


//flux d'emission , comsommateur du buffeur (chaque client a le sien)   
public class ShoutcastOutput implements ShoutcastProtocol{
	PrintWriter os;
	public ShoutcastOutput(OutputStream out) throws IOException {
		this.os = new PrintWriter(out, true);
	}
	@Override
	public synchronized void sendName(String name) {
		os.println("NAME");
		os.println(name);
		
	}
	@Override
	public synchronized void sendNameOK() {
		os.println("NAME OK");
	}
	@Override
	public synchronized void sendNameBad() {
		os.println("NAME BAD");
		
	}
	@Override
	public synchronized void sendMessage(String user, String msg) {
		os.println("MESSAGE");
		os.println(user);
		os.println(msg);
	}
	@Override
	public synchronized void sendAskUserList() {
		os.println("AULIST");
	}
	@Override
	public void sendUserList(Collection<String> ulist) {
		os.println("ULIST");
		ulist.forEach(os::println);
		os.println(".");
	}
	@Override
	public synchronized void sendPrivateMessage(String user, String to,String msg) {
		os.println("PRIVATE MESSAGE");
		os.println(user);
		os.println(to);
		os.println(msg);
	}
	@Override
	public synchronized void sendQuit() {
		os.println("QUIT");
	}
	
	@Override
	public synchronized void sendRoomOK(String room) {
		os.println("ROOM OK");
		os.println(room);
	}
	@Override
	public synchronized void sendRoomBad(String room) {
		os.println("ROOM BAD");
		os.println(room);
	}
	@Override
	public synchronized void sendCreateRoom(String room) {
		os.println("CREATE ROOM");
		os.println(room);
	}
	@Override
	public synchronized void sendDeleteRoom(String room) {
		os.println("DELETE ROOM");
		os.println(room);
	}
	@Override
	public synchronized void sendAskRoomList() {
		os.println("ARLIST");
		ShoutcastModel.getRooms().forEach(os::println);
		os.println(".");
	}
	@Override
	public synchronized void sendRoomList(Collection<String> roomList) {
		os.println("RLIST");
		roomList.forEach(os::println);
		os.println(".");
	}
	@Override
	public synchronized void sendRoomMessage(String room, String user, String msg) {
		os.println("ROOM MESSAGE");
		os.println(room);
		os.println(user);
		os.println(msg);
	}
	@Override
	public synchronized void sendEnterRoom(String room) {
		os.println("ENTER ROOM");
		os.println(room);
	}
	@Override
	public synchronized void sendLeaveRoom(String room) {
		os.println("LEAVE ROOM");
		os.println(room);
	}
	@Override
	public synchronized void sendAskRoomUserList(String room) {
		os.println("ARULIST");
		os.println(room);
	}
	@Override
	public synchronized void sendRoomUserList(String room, Collection<String> userList) {
		os.println("RULIST");
		os.println(room);
		userList.forEach(os::println);
		os.println(".");
	}
	@Override
	public synchronized void sendError(String msg) {
		os.println(msg);
	}
	
}
