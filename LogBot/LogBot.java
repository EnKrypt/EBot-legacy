//   **************************************************************************
//   *                                                                        *
//   *  This program is free software: you can redistribute it and/or modify  *
//   *  it under the terms of the GNU General Public License as published by  *
//   *  the Free Software Foundation, either version 3 of the License, or     *
//   * (at your option) any later version.                                    *
//   *                                                                        *
//   *  This program is distributed in the hope that it will be useful,       *  
//   *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
//   *  MERCHANTABILITY || FITNESS FOR A PARTICULAR PURPOSE.  See the         *
//   *  GNU General Public License for more details.                          *
//   *                                                                        *
//   *  You should have received a copy of the GNU General Public License     *
//   *  along with this program.  If not, see <http://www.gnu.org/licenses/>. *
//   *                                                                        *
//   *         (C) Arvind Kumar 2011 . All rights reserved                    *
//   **************************************************************************

import java.io.*;
import java.net.*;
import java.util.*;

public class LogBot implements Runnable{
	
	static BufferedReader in;
	static BufferedReader b;
	static BufferedWriter out;
	static Socket con;
	static String host;
	static int port;
	static String channel;

	public static void main(String args[])throws IOException{
		LogBot bot=new LogBot();
		bot.go();
	}
	public void go()throws IOException{
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("*****************************LogBot v2.3*******************************\n");
		System.out.println("IRC Logger by EnKrypt\n");
		
		System.out.print("Host: ");
		host=b.readLine();
		System.out.print("Port: ");
		try { 
			port=Integer.parseInt(b.readLine()); 
		}
		catch(Exception e){ 
			System.out.println("Incorrect format for parameter\nTerminating"); 
			System.exit(1); 
		}
		System.out.print("Channel: ");
		channel=b.readLine();
		try {
			con=new Socket(host,port);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		}
		catch (UnknownHostException e) {
            		System.out.println("Host not found: "+host);
            		System.exit(1);
        	} 
		catch (IOException e) {
            		System.out.println("Connection error for "+host+" on port "+port);
            		System.exit(1);
        	}
		
		out.write("NICK " + "LogBot" + "\r\n");
         	out.write("USER " + "EnKrypt" + " \"\" \"\" :" + "Arvind" + "\r\n");
		out.write("JOIN " + channel + "\r\n");
		out.flush();
		
		new Thread(this).start();
		
		String send="";
		while(true){ 
			send=b.readLine();
			if (send.startsWith("/")){
				out.write(send.substring(1)+"\r\n");
				out.flush(); 
			}
			else{	
				out.write("PRIVMSG "+channel+" :"+send+"\r\n");
				out.flush(); 
			}
		}
	}
	public void run(){
		String get="";
		String mode="";
		int ff=0;
		int fm=0;
		String a="",b="",by="no",full="no";
		try {
			BufferedWriter file=new BufferedWriter(new FileWriter("log.txt",true));
			file.newLine();
			file.write("********************************");
			file.newLine();
			file.newLine();
			file.close();
		}
		catch(Exception e){}
		int cc=-1;
		while(true){ 
			try{
				BufferedWriter file=new BufferedWriter(new FileWriter("log.txt",true));
				by="no";
				full="no";
				get=in.readLine();
				System.out.println(get); 
				cc=get.indexOf(":!");
				if (get.startsWith("PING")){
					out.write("PONG "+get.substring(5)+"\r\n");
					out.flush();
					System.out.println("Ponged");
					get="no";
				}
				if (cc!=-1){
					by=get.substring(1,(get.indexOf("!")));
					a=get.substring(cc+1);
					if (a.startsWith("!nolog ")||a.startsWith("!NOLOG ")){
						b=a.substring(7);
						get="no";
					} 
					if (a.startsWith("!ignore ")||a.startsWith("!IGNORE ")){
						b=a.substring(8);
						get="no";
					} 
					if (a.startsWith("!no ")||a.startsWith("!NO ")){
						b=a.substring(5);
						get="no";
					} 
				}
				if (get.indexOf("are supported by this server")!=-1&&fm==0){
					file.write("Succcessfully connected to "+host);
					file.newLine();
					file.close();
					fm=1;
				}
				if (get.indexOf("372 LogBot :-")!=-1&&ff==0){
					get=host+" Message of the Day : ";
					ff=1;
				}
				if (get.indexOf("372 LogBot :-")!=-1&&ff==1){
					get=get.substring(get.indexOf(":-")+1);
				}
				if (get.indexOf("366 LogBot")!=-1){
					file.write("Joined "+get.substring(get.indexOf("366 LogBot")+11,get.indexOf(":",get.indexOf("366 LogBot"))));
					file.newLine();
					file.newLine();
					file.close();
					get="no";
				}
				
				if (get.startsWith(":irc.localhost")||get.startsWith(":"+host)){
					get="no";
				}
				if (get.startsWith(":")&&get.indexOf("!")!=-1){
					by=get.substring(1,(get.indexOf("!")));
					full=get.substring(1,(get.indexOf(" ")));
				}
				if (get.startsWith(":"+full)&&get.indexOf("ACTION")!=-1){
					get="* "+by+" "+get.substring(get.indexOf("ACTION")+7);
				}
				if (get.startsWith(":"+full+" PRIVMSG ")){
					get=by+" : "+get.substring(get.indexOf(":",2)+1);
				}
				if (get.startsWith(":"+full+" TOPIC ")){
					get=by+" has changed the topic to: "+get.substring(get.indexOf(":",2)+1);
				}
				if (get.startsWith(":"+full+" NICK ")){
					get=by+" is now known as "+get.substring(get.indexOf(":",2)+1);
				}
				if (get.startsWith(":"+full+" JOIN ")){
					get=by+" has joined "+get.substring(get.indexOf(":",2)+1);
				}
				if (get.startsWith(":"+full+" QUIT ")){
					get=by+" has left the channel ("+get.substring(get.indexOf(":",2)+1)+")";
				}
				if (!get.equals("no")){
				file.write(get);
				file.newLine();
				file.close();
				}
				
			}
			catch(Exception e){
				e.printStackTrace(); 
			}
		}
	}
	public void type(String b,String mode)throws IOException{
		if (mode.equals("mor")){
			StringBuilder reverse = new StringBuilder();
			for(int i = b.length(); i > 0; --i) {
				char result = b.charAt(i-1);
				reverse.append(result);
			}
			b=reverse.toString();
		}
		if (mode.equals("1337")){
			b=b.toUpperCase();
			String[] aa={"A","B","C","D","E","G","H","I","K","L","M","N","O","R","S","T","U","V","W"};
			String[] bb={"4","8","<","|>","3","6","|-|","1","|<","|_","/\\/\\","/\\/","0","2","5","7","|_|","\\/","\\/\\/"};
			for (int i=0;i<aa.length;i++){
				b=b.replaceAll(aa[i],bb[i]);
			}
		}
		out.write("PRIVMSG "+channel+" :"+b+"\r\n");
		out.flush();
	}
	public int count(String filename) throws IOException {
        	File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int count = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			count++;
		}
		return count;
    	}
}