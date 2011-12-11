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

public class EBot implements Runnable{
	
	static BufferedReader in;
	static BufferedReader b;
	static BufferedWriter out;
	static Socket con;
	static String host;
	static int port;
	static String channel;

	public static void main(String args[])throws IOException{
		EBot bot=new EBot();
		bot.go();
	}
	public void go()throws IOException{
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("*****************************EBot v1.0*******************************\n");
		System.out.println("IRC Bot by EnKrypt\n");
		
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
		
		out.write("NICK " + "EBot" + "\r\n");
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
		String a="",b="",by="";
		int cc=-1;
		while(true){ 
			try{
				get=in.readLine();
				System.out.println(get); 
				cc=get.indexOf(":!");
				if (get.startsWith("PING")){
					out.write("PONG "+get.substring(5)+"\r\n");
					out.flush();
					System.out.println("Ponged");
				}
				if (cc!=-1){
					by=get.substring(1,(get.indexOf("!")));
					a=get.substring(cc+1);
					System.out.println("Potential command detected");
					if (a.startsWith("!say ")||a.startsWith("!SAY ")){
						type(a.substring(5),mode);
						System.out.println(by+" made EBot say: "+a.substring(5));
					}
					if (a.equalsIgnoreCase("!hello")){
						type("Hello "+by,mode);
						System.out.println("Greeted: "+by);
					}
					if (a.equalsIgnoreCase("!lol")){
						out.write("PRIVMSG "+channel+" :\u0001ACTION lols\u0001\r\n");
						out.flush();
						System.out.println(by+" made EBot lol");
					}
					if (a.startsWith("!random ")||a.startsWith("!RANDOM ")){
						int f1=0;
						int rand=0;
						try{
							rand=Integer.parseInt(a.substring(8));
						}
						catch(Exception e){
							type("Invalid parameter",mode);
							f1=1;
						}
						if (f1==0){	
						double r=Math.random()*rand;
						type("Random number between 0 and "+rand+": "+(int)r,mode);	
						System.out.println(by+" rolled random in range "+rand+" and got "+(int)r);
						}
					}
					if (a.startsWith("!prime ")||a.startsWith("!PRIME ")){
						int f1=0;
						int rand=0;
						try{
							rand=Integer.parseInt(a.substring(7));
						}
						catch(Exception e){
							type("Invalid parameter",mode);
							f1=1;
						}
						if (f1==0){
							if (rand==1||rand==0){
								type("0 and 1 is neither prime nor composite",mode);
								System.out.println(by+" wanted to see if 0 or 1 was prime or not");
							}
							else if (rand<0){
								type("Negative numbers cannot be Prime",mode);
								System.out.println(by+" wanted to test the primality of negative numbers");
							}
							else{
								int i;	
								for (i=2; i < rand ;i++ ){
									int n=rand%i;
									if (n==0){
										type("Not prime : "+i,mode);
										System.out.println(by+" din't know that "+rand+" was not prime");
										break;
									}
								}
								if (i==rand){
									type(rand+" is Prime",mode);	
									System.out.println(by+" found out that "+rand+" was prime");
								}
							}
						}
					}
					if (a.startsWith("!pick ")||a.startsWith("!PICK ")){
						b=a.substring(6);
						String[] ch=b.split(",");	
						double rr=Math.random()*ch.length;
						int r=(int)rr;
						type("EBot picks "+ch[r],mode);
						System.out.println("Picked "+ch[r]+" from "+b+" for "+by);
					}
					if (a.startsWith("!raw ")||a.startsWith("!RAW ")){
						b=a.substring(5);
						if (by.equals("EnKrypt")){
							out.write(b+"\r\n");
							out.flush();
							System.out.println("Executing raw: "+b);
						}
						else{
							out.write("PRIVMSG "+channel+" ^lol\r\n");
							out.flush();
							System.out.println(by+" tried to raw: "+b);
						}
					}
					if (a.equalsIgnoreCase("!version")||a.equalsIgnoreCase("!info")||a.equalsIgnoreCase("!whomademe")||a.equalsIgnoreCase("!author")){
						type("EBot v2.4  Coded by EnKrypt",mode);
						type("EnKrypt IRC Bot",mode);
						System.out.println(by+" now knows more about EBot");
					}
					if (a.startsWith("!ip ")||a.startsWith("!IP ")){
						b=a.substring(4);
						InetAddress dom = java.net.InetAddress.getByName(b);
						type(b+" translates to "+dom.getHostAddress(),mode);
						System.out.println(by+" found the ip of "+b+" to be "+dom.getHostAddress());
					}
					if (a.startsWith("!reverse ")||a.startsWith("!REVERSE ")){
						b=a.substring(9);
						type(b,"mor");
						System.out.println(by+" reversed "+b);
					}
					if (a.startsWith("!rev ")||a.startsWith("!REV ")||a.startsWith("!mor ")||a.startsWith("!MOR ")){
						b=a.substring(5);
						type(b,"mor");
						System.out.println(by+" reversed "+b);
					}
					if (a.startsWith("!1337 ")||a.startsWith("!leet ")||a.startsWith("!LEET ")){
						b=a.substring(6);
						type(b,"1337");
						System.out.println(by+" 1337: "+b);
					}
					if (a.startsWith("!quote ")||a.startsWith("!QUOTE ")){
						b=a.substring(7);
						int f1=0;	
						try{
							if (b.trim()==null||b.trim().equals("")){
								f1=1;
							}
						}
						catch(NullPointerException n){
							f1=1;
						}
						if (f1==0){
							BufferedWriter l=new BufferedWriter(new FileWriter("quotes.txt",true));
							l.newLine();
							l.write("\""+b+"\" ~ "+by);
							l.close();
							type("Quote saved",mode);
							System.out.println(by+" saved quote "+b);
						}
					}
					if (a.equalsIgnoreCase("!quote")){
						int size=count("quotes.txt");
						double ran=Math.random()*size;
						int rand=(int)ran;
						BufferedReader l=new BufferedReader( new FileReader("quotes.txt"));
						String info = "";
						if (rand==0){
							rand=1;
						}
						for (int i = 0; i < rand; i++){
							info = l.readLine();
						}
						l.close();
						type(info,mode);
						System.out.println(by+" got a quote: "+info+" at line "+rand);
					}
					if (a.startsWith("!binary ")||a.startsWith("!BINARY ")){
						int f1=0;
						int rand=0;
						try{
							rand=Integer.parseInt(a.substring(8));
						}
						catch(Exception e){
							type("Invalid parameter",mode);
							f1=1;
						}
						if (f1==0){
							String bin = Integer.toBinaryString(rand);
							type(bin,mode);
							System.out.println(by+" found out that the binary of "+rand+" was "+bin);
						}
					}
					if (a.startsWith("!octal ")||a.startsWith("!OCTAL ")){
						int f1=0;
						int rand=0;
						try{
							rand=Integer.parseInt(a.substring(7));
						}
						catch(Exception e){
							type("Invalid parameter",mode);
							f1=1;
						}
						if (f1==0){
							String bin = "0"+Integer.toOctalString(rand);
							type(bin,mode);
							System.out.println(by+" found out that the octal of "+rand+" was "+bin);
						}
					}
					if (a.startsWith("!hex ")||a.startsWith("!HEX ")){
						int f1=0;
						int rand=0;
						try{
							rand=Integer.parseInt(a.substring(5));
						}
						catch(Exception e){
							type("Invalid parameter",mode);
							f1=1;
						}
						if (f1==0){
							String bin = "0x"+Integer.toHexString(rand).toUpperCase();
							type(bin,mode);
							System.out.println(by+" found out that the hex of "+rand+" was "+bin);
						}
					}
					if (a.startsWith("!you ")||a.startsWith("!YOU ")){
						out.write("PRIVMSG "+channel+" :\u0001ACTION "+a.substring(5)+"\u0001\r\n");
						out.flush();
						System.out.println(by+" made EBot do: "+a.substring(5));
					}
					if (a.startsWith("!mode ")||a.startsWith("!MODE ")){
						b=a.substring(6);
						if (b.equalsIgnoreCase("mor")){
							if (mode.equals("mor")){
								mode="";
								type("Mor modifier deactivated",mode);
								System.out.println(by+" deactivated mor mode");
							}
							else{
								mode="mor";
								type("Mor modifier activated",mode);
								System.out.println(by+" activated "+mode+" mode");
							}
						}
						if (b.equalsIgnoreCase("1337")){
							if (mode.equals("1337")){
								mode="";
								type("1337 modifier deactivated",mode);
								System.out.println(by+" deactivated 1337 mode");
							}
							else{
								mode="1337";
								type("1337 modifier activated",mode);
								System.out.println(by+" activated "+mode+" mode");
							}
						}
					}
					if (a.equalsIgnoreCase("!help")||a.equalsIgnoreCase("!commands")){
						type("Commands for EBot: ",mode);
						type("!say <text> . Makes EBot say text. ",mode);
						type("!hello . Greet EBot ",mode);
						type("!lol . Make EBot lol ",mode);
						type("!help . Shows the help page ",mode);
						type("!binary <number> . Shows the binary conversion of the number ",mode);	
						type("!octal <number> . Shows the octal conversion of the number ",mode);
						try{
							Thread.sleep(5000);
						}
						catch(Exception e){
						}
						type("!hex <number> . Shows the hexadecimal conversion of the number ",mode);
						type("!prime <integer> . Checks the primality of the number ",mode);
						type("!random <limit> . Gives a random number from 0 to the limit specified ",mode);
						type("!pick <1starg,2ndarg....,ntharg> . Randomly picks out a choice from the arguments ",mode);
						type("!info . Short description of EBot ",mode);
						type("!ip <domain> . Converts domain to IP address ",mode);
						type("!reverse <text> . Reverses text ",mode);
						type("!1337 <text> . Converts text into leetspeak ",mode);
						type("!quote . Makes EBot say a random quote from the list of quotes ",mode);
						type("!quote <quote> . Adds the quote to the list of quotes ",mode);
						type("!you <text> . Makes EBot rp the text ",mode);
						type("!mode <mode> . Starts a modifier ",mode);
						type(" ",mode);
						try{
							Thread.sleep(30000);
						}
						catch(Exception e){
						}
						type("Modifiers: ( to be used with !mode )",mode);
						type("mor : Reveres all text given by EBot",mode);
						type("1337 : Converts all text given by EBot to leetspeak",mode);
						type(" ",mode);
						type("All commands are case insensitive. ",mode);
						type("Single command help is not available currently. ",mode);
						type("There are a few more additional commands that are meant for administration. ",mode);
						type("And also a few that do the same as above. Example !mor and !rev function the same as !reverse. ",mode);
						type(" ",mode);
						type("The person running EBot can also chat using console ",mode);
						type("Typing anything normally will function as a normal PRIVMSG to the current channel ",mode);
						type("Prepending the text with a \"/\" will send a direct raw command ",mode);
						type("----------------------End of Help--------------------- ",mode);
						System.out.println(by+" needed help with EBot");
					}
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
			String[] aa={"a","A","e","E","g","G","i","I","r","o","O","R","s","S","t","T"};
			String[] bb={"4","4","3","3","6","6","1","1","2","0","0","2","5","5","7","7"};
			for (int i=0;i<aa.length;i++){
				b=b.replaceAll(aa[i],bb[i]);
			}
			b=b.toUpperCase();
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