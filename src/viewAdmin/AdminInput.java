package viewAdmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


//ordre de l'admin pour l'ordre de diffusion
public class AdminInput {
	boolean stop = false;
	ShoutcastProtocol handler;
	InputStream in;
	
	public AdminInput(InputStream in, ShoutcastProtocol handler) throws IOException {
		this.in = in;
		this.handler = handler;
	}
	
	public void doRun() throws IOException, ShoutcastProtocolException {
		String strMsg, strRoom, strName = null;
		boolean stop = false;
		ArrayList<String> userList;
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in))) {
			while(!stop) {
				String line = is.readLine();
				if(line != null){
					switch (line) {
					case "NAME":
						strName = is.readLine();
						handler.sendName(strName);
						break;
					case "NAME OK":
						handler.sendNameOK();
						break;
					case "NAME BAD":
						handler.sendNameBad();
						break;
					case "MESSAGE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.sendMessage(strName, strMsg);
						break;
					case "PRIVATE MESSAGE":
						strName = is.readLine();
						String strDest = new String(is.readLine());
						strMsg = is.readLine();
						handler.sendPrivateMessage(strName,strDest, strMsg);
						break;
					case "AULIST":
						handler.sendAskUserList();
						break;
					case "ULIST":
						List<String> ulist = new ArrayList<>();
						while(!(strName = is.readLine()).equals("."))
							ulist.add(strName);
						handler.sendUserList(ulist);
						break;
					case "QUIT":
						handler.sendQuit();
						break;
					case "CREATE ROOM":
						strRoom = is.readLine();
						handler.sendCreateRoom(strRoom);
						break;
					case "ROOM OK":
						strRoom = is.readLine();
						handler.sendRoomOK(strRoom);
						break;
					case "ROOM BAD":
						strRoom = is.readLine();
						handler.sendRoomBad(strRoom);
						break;
					case "DELETE ROOM":
						strRoom = is.readLine();
						handler.sendDeleteRoom(strRoom);
						break;
					case "ARLIST":
						handler.sendAskRoomList();
						break;
					case "RLIST":
						List<String> rlist = new ArrayList<>();
						while(!(strRoom = is.readLine()).equals("."))
							rlist.add(strRoom);
						handler.sendRoomList(rlist);
						break;
					case "ROOM MESSAGE":
						strRoom = is.readLine();
						strName = is.readLine();
						strMsg = is.readLine();
						handler.sendRoomMessage(strRoom, strName, strMsg);
						break;
					case "ENTER ROOM":
						strRoom = is.readLine();
						handler.sendEnterRoom(strRoom);
						break;
					case "LEAVE ROOM":
						strRoom = is.readLine();
						handler.sendLeaveRoom(strRoom);
						break;
					case "ARULIST":
						strRoom = is.readLine();
						handler.sendAskRoomUserList(strRoom);
						break;
					case "RULIST":
						strRoom = is.readLine();
						List<String> rulist = new ArrayList<>();
						while(!(strName = is.readLine()).equals("."))
							rulist.add(strName);
						handler.sendRoomUserList(strRoom, rulist);
						break;
					case "ERR":
						strMsg = is.readLine();
						handler.sendError(strMsg);
						break;
					default:
						throw new ShoutcastProtocolException("Invalid input");
					}	
				}
				else {
					stop = true;
					throw new ShoutcastProtocolException("Connection lost");
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
