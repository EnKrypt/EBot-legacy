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
//   *         (C) Arvind Kumar 2011 .                                        *
//   *         (C) James McClain 2011 .                                       *
//   **************************************************************************

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EBot extends JFrame implements Runnable{
	
	static BufferedReader in;
	static BufferedReader b;
	static BufferedWriter out;
	static Socket con;
	static String host;
	static int port;
	static String channel;
	JTextArea incoming;
	JTextField outgoing;
	static String expects;
	static Pattern lisp;
	static Matcher now;
	static Pattern quo;
	static Matcher nowa;
	static Pattern elisp;
	static Matcher enow;
	static Pattern equo;
	static Matcher enowa;
	static Map var=new HashMap();
	static Map mkdev=new HashMap();
	static String result;
	static String eresult;

	EBot(){
		JFrame frame=new JFrame("EBot - IRC Bot");
		JPanel panel=new JPanel();
		incoming=new JTextArea(23,120);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane scroll=new JScrollPane(incoming, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
				incoming.select(incoming.getHeight()+10000,0);
			}
		});	
		outgoing=new JTextField(65);
		JButton send=new JButton("Send");
		send.addActionListener(new SendButtonListener());
		outgoing.addActionListener(new SendButtonListener());
		Font font=new Font("Courier",Font.PLAIN,12);
		incoming.setFont(font);
		panel.add(scroll);
		panel.add(outgoing);
		panel.add(send);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(900,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		outgoing.requestFocusInWindow();
	}

	public static void main(String args[])throws IOException{
		EBot bot=new EBot();
		bot.go();
	}
	public void show(String sho){
		incoming.append(sho+"\n");
	}
	public void go()throws IOException{
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		
		show("****************************************************EBOT - FOR IRC****************************************************\n");
		show("IRC Bot by EnKrypt\n");
		show("Version 4.5\n");
		
		incoming.append("Host: ");
		expects="host";
	}
	public void go1()throws IOException{
		outgoing.requestFocusInWindow();
		incoming.append("Port: ");
		expects="port";
	}
	public void go2()throws IOException{
		outgoing.requestFocusInWindow();
		incoming.append("Channel: ");
		expects="channel";
	}
	public void go3()throws IOException, InterruptedException{
		try {
			con=new Socket(host,port);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		}
		catch (UnknownHostException e) {
            		show("Host not found: "+host+". Substituting with localhost");
			host="localhost";
            		con=new Socket("localhost",port);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        	} 
		catch (IOException e) {
            		show("Connection error for "+host+" on port "+port+". \n");
			expects="error";
			return ;
        	}
		
		out.write("NICK " + "EBot" + "\r\n");
         	out.write("USER " + "EnKrypt" + " \"\" \"\" :" + "Arvind" + "\r\n");
		out.write("JOIN " + channel + "\r\n");
		out.flush();
		
		new Thread(this).start();
		
		expects="send";
	}
	public void run(){
		String get="";
		String mode="";
		String a="",b="",by="",full="";
		int ff=0;
		int fm=0;
		int cc=-1;
		int fr=0;
		while(true){ 
			try{
				get=in.readLine();
				cc=get.indexOf(":!");
				if (get.startsWith("PING")){
					out.write("PONG "+get.substring(5)+"\r\n");
					out.flush();
			 		get="no";
				}
				if (cc!=-1){
					by=get.substring(1,(get.indexOf("!")));
					a=get.substring(cc+1);
					if (a.startsWith("!say ")||a.startsWith("!SAY ")){
						type(a.substring(5),mode);
						show(by+" made EBot say: "+a.substring(5));
					}
					if (a.equalsIgnoreCase("!hello")){
						type("Hello "+by,mode);
						show("Greeted: "+by);
					}
					if (a.equalsIgnoreCase("!lol")){
						out.write("PRIVMSG "+channel+" :\u0001ACTION lols\u0001\r\n");
						out.flush();
						show(by+" made EBot lol");
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
						show(by+" rolled random in range "+rand+" and got "+(int)r);
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
								show(by+" wanted to see if 0 or 1 was prime or not");
							}
							else if (rand<0){
								type("Negative numbers cannot be Prime",mode);
								show(by+" wanted to test the primality of negative numbers");
							}
							else{
								int i;	
								for (i=2; i < rand ;i++ ){
									int n=rand%i;
									if (n==0){
										type("Not prime : "+i,mode);
										show(by+" din't know that "+rand+" was not prime");
										break;
									}
								}
								if (i==rand){
									type(rand+" is Prime",mode);	
									show(by+" found out that "+rand+" was prime");
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
						show("Picked "+ch[r]+" from "+b+" for "+by);
					}
					if (a.startsWith("!raw ")||a.startsWith("!RAW ")){
						b=a.substring(5);
						if (by.equals("EnKrypt")){
							out.write(b+"\r\n");
							out.flush();
							String resp=in.readLine();
							resp=resp.substring(resp.lastIndexOf(":")+1);
							type(resp,mode);
							show("Executing raw: "+b+" Got: "+resp);
						}
						else{
							out.write("PRIVMSG "+channel+" ^lol\r\n");
							out.flush();
							show(by+" tried to raw: "+b);
						}
					}
					if (a.equalsIgnoreCase("!version")||a.equalsIgnoreCase("!info")||a.equalsIgnoreCase("!whomademe")||a.equalsIgnoreCase("!author")){
						type("EBot v4.5  Coded by EnKrypt . Last update on 04.06.13",mode);
						type("Lambda , RunLambda made by JamezQ",mode);
						type(" - EnKrypt IRC Bot",mode);
						show(by+" now knows more about EBot");
					}
					if (a.startsWith("!ip ")||a.startsWith("!IP ")){
						b=a.substring(4);
						InetAddress dom = java.net.InetAddress.getByName(b);
						type(b+" translates to "+dom.getHostAddress(),mode);
						show(by+" found the ip of "+b+" to be "+dom.getHostAddress());
					}
					if (a.startsWith("!reverse ")||a.startsWith("!REVERSE ")){
						b=a.substring(9);
						type(b,"mor");
						show(by+" reversed "+b);
					}
					if (a.startsWith("!rev ")||a.startsWith("!REV ")||a.startsWith("!mor ")||a.startsWith("!MOR ")){
						b=a.substring(5);
						type(b,"mor");
						show(by+" reversed "+b);
					}
					if (a.startsWith("!1337 ")||a.startsWith("!leet ")||a.startsWith("!LEET ")){
						b=a.substring(6);
						type(b,"1337");
						show(by+" 1337: "+b);
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
							show(by+" saved quote "+b);
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
						show(by+" got a quote: "+info+" at line "+rand);
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
							show(by+" found out that the binary of "+rand+" was "+bin);
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
							show(by+" found out that the octal of "+rand+" was "+bin);
						}
					}
					if (a.startsWith("!pl ")||a.startsWith("!PL ")){
						String reg=a.substring(4);
						String fina=dot(reg);
						type("Result: "+fina,mode);
						show(by+" Used Lisp to evaluate "+reg);
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
							show(by+" found out that the hex of "+rand+" was "+bin);
						}
					}
					if (a.startsWith("!you ")||a.startsWith("!YOU ")){
						out.write("PRIVMSG "+channel+" :\u0001ACTION "+a.substring(5)+"\u0001\r\n");
						out.flush();
						show(by+" made EBot do: "+a.substring(5));
					}
					if (a.startsWith("!mode ")||a.startsWith("!MODE ")){
						b=a.substring(6);
						if (b.equalsIgnoreCase("mor")){
							if (mode.equals("mor")){
								mode="";
								type("Mor modifier deactivated",mode);
								show(by+" deactivated mor mode");
							}
							else{
								mode="mor";
								type("Mor modifier activated",mode);
								show(by+" activated "+mode+" mode");
							}
						}
						if (b.equalsIgnoreCase("1337")){
							if (mode.equals("1337")){
								mode="";
								type("1337 modifier deactivated",mode);
								show(by+" deactivated 1337 mode");
							}
							else{
								mode="1337";
								type("1337 modifier activated",mode);
								show(by+" activated "+mode+" mode");
							}
						}
					}
					if (a.equalsIgnoreCase("!help")||a.equalsIgnoreCase("!commands")){
						type("To prevent spam and excess flood , the in-built help has been removed.",mode);
						type("Please refer here for help and commands - https://raw.github.com/EnKrypt/EBot/master/Help.txt",mode);
						show(by+" needed help with EBot");
					} 
				}
				
				if (get.indexOf("are supported by this server")!=-1&&fm==0){
					show("Succcessfully connected to "+host);
					fm=1;
				}
				if (get.indexOf("372 EBot :-")!=-1&&ff==0){
					get=host+" Message of the Day : ";
					ff=1;
				}
				if (get.indexOf("372 EBot :-")!=-1&&ff==1){
					get=get.substring(get.indexOf(":-")+1);
				}
				if (get.indexOf("366 EBot")!=-1){
					show("Joined "+get.substring(get.indexOf("366 EBot")+9,get.indexOf(":",get.indexOf("366 EBot"))));
					fr=1;
				}
				if (get.startsWith(":irc.localhost")||get.startsWith(":"+host)){
					if (fr==1)
						get=get.substring(get.lastIndexOf(":")+1);
					else
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
					if (!by.equalsIgnoreCase("ebot")){
						type("Welcome to IRC , "+by,"");
					}
				}
				if (get.startsWith(":"+full+" QUIT ")){
					get=by+" has left the channel ("+get.substring(get.indexOf(":",2)+1)+")";
				}
				if (get.startsWith(":"+full+" PART ")){
					get=by+" has left the channel ("+get.substring(get.indexOf(":",2)+1)+")";
				}
				if (!get.equals("no")){
				show(get);
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
			String[] aa={"A","B","C","D","E","G","H","I","K","L","M","N","O","R","S","T","U","V","W","Y"};
			String[] bb={"4","8","(","|>","3","6","|-|","1","|<","|_","/\\\\/\\\\","/\\\\/","0","2","5","7","|_|","\\\\/","\\\\/\\\\/","'/"};
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
	public boolean hasfile(String filename,String arg) throws IOException {
        File file = new File(filename);
		Scanner scanner = new Scanner(file);
		boolean count = false;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.equals(arg))
				count=true;
		}
		return count;
    }
	public String dot(String code){
        Pattern lisp;
        Matcher now;
        Pattern quo;
        Matcher nowa;
		quo=Pattern.compile("'[^'\"]*\"");
		nowa=quo.matcher(code);
		code=unquote(code);
		lisp=Pattern.compile("\\([^()]*\\)");
		now=lisp.matcher(code);
		while (now.find()){
			String m=now.group(0);
			m=m.replace("(","");
			m=m.replace(")","");
            m=m.replaceAll(" +"," ");
			String[] arg=m.split(" ");
			String res=eval(arg);
			result = now.replaceFirst(res);
			return dot(result);
		}
		String[] cfin=code.split(" ");
        if (cfin.length != 0)
            return cfin[cfin.length-1];
        return "";
	}
	public String eval(String arg[]){
		arg=requote(arg);
                 // for(int i=0;i<arg.length;++i){
                 //     System.out.println(i+" : "+arg[i]);
                 // }
		if (arg[0].equalsIgnoreCase("add")){
			double cres=0;
			for (int i=1;i<arg.length;i++){
				cres+=Double.parseDouble(arg[i]);
			}
			return ""+cres;
		}
		else if (arg[0].equalsIgnoreCase("sub")){
			double cres=Double.parseDouble(arg[1]);
			for (int i=2;i<arg.length;i++){
				cres-=Double.parseDouble(arg[i]);
			}
			return ""+cres;
		}
		else if (arg[0].equalsIgnoreCase("mul")){
			double cres=Double.parseDouble(arg[1]);;
			for (int i=2;i<arg.length;i++){
				cres*=Double.parseDouble(arg[i]);
			}
			return ""+cres;
		}
		else if (arg[0].equalsIgnoreCase("div")){
			double cres=Double.parseDouble(arg[1]);
			for (int i=2;i<arg.length;i++){
				cres/=Double.parseDouble(arg[i]);
			}
			return ""+cres;
		}
		else if (arg[0].equalsIgnoreCase("cat")){
			String cres="";
			for (int i=1;i<arg.length;i++){
				cres+=arg[i];
			}
			return cres;
		}
		else if (arg[0].equalsIgnoreCase("eval")){
			String cres="";
            arg[0] = "";
            cres = combine(arg," ");
            cres=dot(cres);
            return cres;
		}
		else if (arg[0].equalsIgnoreCase("slice")){
			String cres="";
			cres=arg[1].substring(Integer.parseInt(arg[2]),Integer.parseInt(arg[3])+1);
			return cres;
		}
		else if (arg[0].equalsIgnoreCase("eq")){
			String cres="";
			String eqchk=arg[1];
			int flag=1;
			for (int i=1;i<arg.length;i++){
				if (!arg[i].equalsIgnoreCase(eqchk)){
					flag=0;
				}
			}
			return flag+"";
		}
		else if (arg[0].equalsIgnoreCase("gt")){
			String cres="";
			int flag=1;
			for (int i=2;i<arg.length;i++){
				if ((Math.max(Double.parseDouble(arg[i-1]),Double.parseDouble(arg[i]))!=Double.parseDouble(arg[i-1]))||Double.parseDouble(arg[i])==Double.parseDouble(arg[i-1])){
					flag=0;
				}
			}
			return flag+"";
		}
		else if (arg[0].equalsIgnoreCase("lt")){
			String cres="";
			int flag=1;
			for (int i=2;i<arg.length;i++){
				if ((Math.min(Double.parseDouble(arg[i-1]),Double.parseDouble(arg[i]))!=Double.parseDouble(arg[i-1]))||Double.parseDouble(arg[i])==Double.parseDouble(arg[i-1])){
					flag=0;
				}
			}
			return flag+"";
		}
		else if (arg[0].equalsIgnoreCase("strlen")){
			return ""+arg[1].length();
		}
		else if (arg[0].equalsIgnoreCase("and")){
			String cres="";
			int flag=1;
			for (int i=1;i<arg.length;i++){
				if (arg[i].equalsIgnoreCase("0")||arg[i].equalsIgnoreCase("0.0")){
					flag=0;
				}
			}
			return flag+"";
		}
		else if (arg[0].equalsIgnoreCase("or")){
			String cres="";
			int flag=0;
			for (int i=1;i<arg.length;i++){
				if (!arg[i].equalsIgnoreCase("0")&&!arg[i].equalsIgnoreCase("0.0")){
					flag=1;
				}
			}
			return flag+"";
		}
		else if (arg[0].equalsIgnoreCase("not")){
			String cres="";
			if (arg[1].equalsIgnoreCase("0")||arg[1].equalsIgnoreCase("0.0")){
				return "1";
			}
			else{
				return "0";
			}
		}
		else if (arg[0].equalsIgnoreCase("print")){
			String cres="";
			for (int i=1;i<arg.length;i++){
				cres+=arg[i]+" ";
			}
			try{
                type(cres,"");
			}
			catch(Exception e){ }
			return "";
		}
		else if (arg[0].equalsIgnoreCase("read")&&arg.length==2){
			String cres="";
			for (int i=1;i<arg.length;i++){
				cres+=arg[i]+" ";
			}
			if (cres.equals(" ")){
				try{
					cres=in.readLine();
					cres=cres.substring(cres.lastIndexOf(":")+1);
				}
				catch(Exception e) {}
			}
			else{
				try{
					type(cres,"");
					cres=in.readLine();
					cres=cres.substring(cres.lastIndexOf(":")+1);
				}
				catch(Exception e) {}
			}
			return "'"+cres+"\"";
		}
		else if (arg[0].equalsIgnoreCase("read")&&arg.length==1){
			String cres="";
			try{
				cres=in.readLine();
				cres=cres.substring(cres.lastIndexOf(":")+1);
			}
			catch(Exception e) {}
			return "'"+cres+"\"";
		}
		else if (arg[0].equalsIgnoreCase("read-url")){
			String lin="",cres="";
			try{
				URL url=new URL(arg[1]);
				BufferedReader read=new BufferedReader(new InputStreamReader(url.openStream()));
				while ((lin=read.readLine())!=null){
					cres+=lin+" ";
				}
				read.close();
			}
			catch(Exception e){ e.printStackTrace(); }
			return "'"+cres+"\"";
		}
		else if (arg[0].equalsIgnoreCase("save")){
			String lin="",cres="";
			try{
				File fil=new File(arg[1]);
				if (!fil.exists()){
					fil.createNewFile();
				}
				BufferedWriter read=new BufferedWriter(new FileWriter(arg[1]));
				Set cvar = var.keySet();
				Set cmkdev = mkdev.keySet();
				Iterator itrv = cvar.iterator();
				Iterator itrm = cmkdev.iterator();
				while (itrv.hasNext()){
					String nex=itrv.next().toString();
					read.write("(var "+nex+" '"+var.get(nex)+"\")");
					read.newLine();
					read.flush();
				}
				while (itrm.hasNext()){
					String nex=itrm.next().toString();
					read.write("(mkdev "+nex+" '"+mkdev.get(nex)+"\")");
					read.newLine();
					read.flush();
				}
				read.close();
			}
			catch(Exception e){ e.printStackTrace(); }
			return "(eval '"+cres+"\")";
		}
		else if (arg[0].equalsIgnoreCase("include")){
			String lin="",cres="";
			try{
				BufferedReader read=new BufferedReader(new FileReader(arg[1]));
				while ((lin=read.readLine())!=null){
					cres+=lin+" ";
				}
				read.close();
			}
			catch(Exception e){ e.printStackTrace(); }
			return "(eval '"+cres+"\")";
		}
		else if (arg[0].equalsIgnoreCase("mkdev")&&arg.length==3){
			mkdev.put(arg[1],arg[2]);
			return "";
		}
		else if (arg[0].equalsIgnoreCase("pass")){
			String cres="";
			return "";
		}
		else if (arg[0].equalsIgnoreCase("if")){
			if (arg[1].equalsIgnoreCase("0")||arg[1].equalsIgnoreCase("0")){
				return arg[3];
			}
			else{
				return arg[2];
			}
		}
		else if (arg[0].equalsIgnoreCase("var")&&arg.length==3){
			var.put(arg[1],arg[2]);
			return "";
		}
		else if (arg[0].equalsIgnoreCase("var")&&arg.length==2){
			String setvar="";
			try{
				setvar=var.get(arg[1]).toString();
			}
			catch(NullPointerException npe){
				setvar="0";
			}
			return ""+setvar;
		}
        else if (arg[0].equalsIgnoreCase("lambda")){
            return "runlambda '"+arg[1]+"\" '"+arg[2]+"\"";
        }
        else if (arg[0].equalsIgnoreCase("runlambda")){
            arg[1] = arg[1].replaceFirst("^\\(","");
            arg[1] = arg[1].replaceFirst("\\)$","");
            arg[1] = arg[1].replaceAll(" +"," ");
            String[] lambdaArg = arg[1].split(" ");
            String to_eval;
            to_eval = arg[2];
            to_eval = to_eval.replaceAll("\\("," ( ");
            to_eval = to_eval.replaceAll("\\)"," ) ");
            to_eval = to_eval.replaceAll("'"," ' ");
            to_eval = to_eval.replaceAll("\""," \" ");
            int i;
            int q;
            int argNum = 3;
            String[] to_eval_array;
            for(i=0,q=0; i < lambdaArg.length;++i,++argNum){
                to_eval_array = to_eval.split(" ");
                for(q=0;q < to_eval_array.length;++q){
                    if(to_eval_array[q].equals(lambdaArg[i])) {
                        to_eval_array[q] = arg[argNum];
                    }
                }
                to_eval = combine(to_eval_array," ");
            }
            to_eval = to_eval.replaceAll(" \\( ","(");
            to_eval = to_eval.replaceAll(" \\) ",")");
            to_eval = to_eval.replaceAll(" ' ","'");
            to_eval = to_eval.replaceAll(" \" ","\"");
            return "(eval '"+to_eval+"\")";
        }
		else{
			String devi="";
			String param="";
			for (int i=1;i<arg.length;i++){
				param+=" "+arg[i];
			}
			try{
				devi=mkdev.get(arg[0]).toString();
			}
			catch(Exception npe){
				return "";
			}
            return "("+devi+param+")";
		}
	}
	public String[] requote(String[] ar) {
		for(int i=0;i<ar.length;i++) {
    			ar[i] = ar[i].replaceAll("---","'");
  			ar[i] = ar[i].replaceAll("~~~","\"");
  			ar[i] = ar[i].replaceAll("\\{","(");
  			ar[i] = ar[i].replaceAll("\\}",")");
  			ar[i] = ar[i].replaceAll("^'","");
    			ar[i] = ar[i].replaceAll("\"$","");
    			ar[i] = ar[i].replaceAll("<>"," ");
  		}
  		return ar;
	}
	public String unquote(String ar) {
            Pattern lisp;
            Matcher now;
            Pattern quo;
            Matcher nowa;
            quo=Pattern.compile("'[^'\"]*\"");
            nowa=quo.matcher(ar);
            while(nowa.find()) {
                String m=nowa.group(0);
                m = m.replaceAll("^.","---");
                m = m.replaceAll(".$","~~~");
                m = m.replaceAll("\\(","{");
                m = m.replaceAll("\\)","}");
                m = m.replaceAll(" ","<>");
                String result = nowa.replaceFirst(m);
                return unquote(result);
            }
            return ar;
	}
    public String combine(String[] s, String glue) {
        int k=s.length;
        if (k==0)
            return null;
        StringBuilder out=new StringBuilder();
        out.append(s[0]);
        for (int x=1;x<k;++x)
            out.append(glue).append(s[x]);
        return out.toString();
    }
public class SendButtonListener implements ActionListener{
	public void actionPerformed(ActionEvent e){
		if (expects.equals("host")){
			host=outgoing.getText();
			show(""+host);
			expects="";
			outgoing.setText("");
			try{
				go1();
				return ;
			}
			catch(Exception n){}
		}
		if (expects.equals("port")){
			try {
				port=Integer.parseInt(outgoing.getText());
			}
			catch(Exception ex){	
				show("Incorrect parameter for port. Substituting with 6667");
				port=6667;
			}
			show(""+port);
			expects="";
			outgoing.setText("");
			try{
				go2();
				return ;
			}
			catch(Exception n){}
		}
		if (expects.equals("channel")){
			channel=outgoing.getText();
			show(""+channel);
			expects="";
			outgoing.setText("");
			try{
				go3();
				return ;
			}
			catch(Exception n){}
		}
		if (expects.equals("send")){
			String send=""; 
			send=outgoing.getText();
			try{
			if (send.startsWith("/")){
				out.write(send.substring(1)+"\r\n");
				out.flush(); 
				show("RAW- "+send.substring(1));
			}
			else{	
				out.write("PRIVMSG "+channel+" :"+send+"\r\n");
				out.flush(); 
				show("EBot : "+send);
			}
			}
			catch(Exception n){}
			outgoing.setText("");
			outgoing.requestFocusInWindow();
		}
		if (expects.equals("error")){
			show("Please restart the application");
			outgoing.setText("");
			try { 
				return ; 
			}
			catch(Exception n){}
		}
	}
} 
}