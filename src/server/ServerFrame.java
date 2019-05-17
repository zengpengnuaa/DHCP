package server;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import javax.swing.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class ServerFrame extends JFrame implements ActionListener,Runnable{     //服务器聊天窗口，实现两个接口，作为动作事件侦听器和线程任务类。     
	Socket soc; //客户端对象，服务器接收到的客户端。       
	JTextArea jta;  //文本区域。  
	JButton jb; //按钮。    
	JScrollPane jsp;    //滚动面板。     
	InputStream in;     //网络字节输入流。  
	OutputStream out;       //网络字节输出流。
	String ip;
	Map<String,String> iptable=new HashMap<String,String>();
	String premac="";
	int flag;
	public ServerFrame(Socket soc) throws IOException{  
		//构造方法，用来初始化对象以及做一些设置。      
	  super("服务器端聊天");  //调用超类JFrame的构造方法设置聊天框的标题。    
	  this.soc=soc;       //传递服务器接收到的客户端套接字。 
	  in=soc.getInputStream();    //服务器的输入流，拿的是客户端的输入流。这样，服务器与客户端进行数据传输便知道是与哪个客户端。         
      out=soc.getOutputStream();  //服务器的输出流，拿的是客户端的输出流。                       
      jta=new JTextArea(20,20);   //初始化文本区域并设置其为20行和20列（一个字符代表一列）。      
      jb=new JButton("发送");     //初始化按钮。   
      jsp=new JScrollPane(jta);   //初始化滚动面板，并把文本区域放置在滚动面板中。               
      this.setLayout(new FlowLayout());   //设置窗体布局为流式布局（此布局为从左向右一次添加组件，一行放不下了转到第二行）。               
      this.add(jb);       //将按钮加到窗体中。    
      this.add(jsp);      //把滚动面板加到窗体中。   
      jb.addActionListener(this);     //为按钮注册动作事件侦听器（当点击按钮时触发动作事件），因为该类实现了动作事件侦听器接口，所以该类对象就是侦听器。      
      this.setBounds(300,300,400,400);    //设置窗体边界和大小。   
      this.setVisible(true);      //设置窗体可见（窗体默认是不可见的）。     
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置窗体默认关闭操作。              
	}
	
	public void actionPerformed(ActionEvent e){   
    	  //ActionListener接口里的方法，必须实现，用来处理当点击按钮或者在文本框按下回车后的动作事件。
    //	  String jfText=jf.getText();     //获取文本框中的内容。
		  
    	  if(ip.length()>0){      //当文本框里面字符串长度大于零时（如果长度为0，则没有意义）执行下面语句。
    		  byte[] by=ip.getBytes();        //将字符串变为字节数组。                       
    		  try {              
    			  out.write(by);      //将字节数组写入网络输出流中，由客户端来接收。                
    			  jta.append("你说："+ip+"\n");   //将服务器的消息显示在文本区内。                   
    			                
    			  } catch (IOException ex) {       
    				  Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);  //一种异常处理，不必深究。         
    				}       
    	  }   
    }   
    public void run(){  //Runnable接口中的方法（必须实现），线程开启后执行的代码。      
    	  while(true){    //因为要不断地接收消息，所以线程要一直运行，所以用while(true)。        
    		  byte[] by=new byte[1024];   //用来接收客户端发来的消息。 
    		  java.util.Arrays.fill(by, (byte) 0);
    		  try {
    			  
    			  int count=in.read(by);      //用网络输入流读取来自客户端的消息，返回读取的有效字节个数。                
    		      jta.setText("客户端说："+new String(by,0,count)+"\n");  //将客户端发来的消息显示在文本区中。
    		      String mac=new String(by,0,count);
    		      //String s[] = mac.split("\\s+");
    		      //mac = s[0];
    		      
    			  if(mac.equals("exit")) {
    				  FileInputStream fis=new FileInputStream("data/Iptable");
        		      InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
        		      BufferedReader br=new BufferedReader(isr);
        		      String line="";
        		      while((line=br.readLine())!=null) {
        		    	  String [] arr=line.split("\\s+");
        		    	  iptable.put(arr[1], arr[0]);
        		      }
        		      String tempip=iptable.get(premac);
   //     		      System.out.println(tempip);
        		      iptable.remove(premac);
        		      System.out.println(iptable);
        		      StringBuffer b2=new StringBuffer();
        		      FileWriter w2=new FileWriter("data/Ippool",true);
        		      b2.append(tempip+"\r\n");
        		      w2.write(b2.toString());
        		      w2.close();
        		      
        		      FileWriter writer = new FileWriter("data/Iptable", false);
        		      StringBuffer buffer = new StringBuffer();
        		      for(Map.Entry entry:iptable.entrySet()){
        		          String key = (String) entry.getKey();
        		          String value = (String) entry.getValue();
        		          buffer.append(value + "  " + key+"\r\n");
        		      }
        		      writer.write(buffer.toString());
        		      writer.close();
        		      
        		      out.close();
    				  this.soc.close();
    				  soc=null;
    				  this.setVisible(false);
    				  return;
    			  }
    			  
    			  
    		      List<String> ippool=new ArrayList<String>();
    		      FileInputStream fis=new FileInputStream("data/Ippool");
    		      InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
    		      BufferedReader br=new BufferedReader(isr);
    		      String line="";
    		      while((line=br.readLine())!=null) {
    		    	  ippool.add(line);
    		      }
    		      if(flag==1) {
    		    	  ip="您已申请到一个ip地址，请勿重复申请";
    		    	  
    		      }
    		      else if(ippool.isEmpty()) {
    		    	  ip="无可用ip";
    		      }
    		      else {
    		    	  int i=0;
    		    	  ip=ippool.get(i);
    		    	  ippool.remove(i);
    		    	  FileOutputStream fos=new FileOutputStream("data/Ippool",false);
    		    	  PrintStream out=new PrintStream(fos);
    		    	  for(int j=0;j<ippool.size();j++){
    		               String str=ippool.get(j)+"\r\n";
    		               out.print(str);
    		          }
    		    	  out.close();
    		    	  FileOutputStream fos1=new FileOutputStream("data/Iptable",true);
    		    	  PrintStream out1=new PrintStream(fos1);
                      out1.print(ip+" "+mac+"\r\n");
                      out1.close();
                      flag=1;
    		      }
    		      premac=mac;
    		      
/*    		      Map<String,String> map=new HashMap<String,String>();
    		      FileInputStream fis=new FileInputStream("data/Iptable");
    		      InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
    		      BufferedReader br=new BufferedReader(isr);
    		      String line="";
    		      while((line=br.readLine())!=null) {
    		    	  String [] arr=line.split("\\s+");
    		    	  System.out.print(arr[0]+"  ");
    		    	  System.out.println(arr[1]);
    		    	  map.put(arr[0], arr[1]);
        	    	  System.out.println(arr[0]);
    		      }
    		      String mac=new String(by);
    		      System.out.println(map.keySet());
    		      System.out.println(mac.getClass().toString());
    		      System.out.println(mac);
    		      System.out.println(map.get((String)mac));
    		      System.out.println(map);
    		      if(map.containsKey("00")) {
    		    	  ip=map.get(mac);
    		    	  System.out.println(ip);
    		      }
    		      else {
    		    	  ip="无可用ip地址";
    		      }*/
    		      } catch (IOException ex) {           
    		    	  Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);  //一种异常处理，不必深究。        
    		        }
    	   }
	}
}


