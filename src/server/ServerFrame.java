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

public class ServerFrame extends JFrame implements ActionListener,Runnable{     //���������촰�ڣ�ʵ�������ӿڣ���Ϊ�����¼����������߳������ࡣ     
	Socket soc; //�ͻ��˶��󣬷��������յ��Ŀͻ��ˡ�       
	JTextArea jta;  //�ı�����  
	JButton jb; //��ť��    
	JScrollPane jsp;    //������塣     
	InputStream in;     //�����ֽ���������  
	OutputStream out;       //�����ֽ��������
	String ip;
	Map<String,String> iptable=new HashMap<String,String>();
	String premac="";
	int flag;
	public ServerFrame(Socket soc) throws IOException{  
		//���췽����������ʼ�������Լ���һЩ���á�      
	  super("������������");  //���ó���JFrame�Ĺ��췽�����������ı��⡣    
	  this.soc=soc;       //���ݷ��������յ��Ŀͻ����׽��֡� 
	  in=soc.getInputStream();    //�����������������õ��ǿͻ��˵�����������������������ͻ��˽������ݴ����֪�������ĸ��ͻ��ˡ�         
      out=soc.getOutputStream();  //����������������õ��ǿͻ��˵��������                       
      jta=new JTextArea(20,20);   //��ʼ���ı�����������Ϊ20�к�20�У�һ���ַ�����һ�У���      
      jb=new JButton("����");     //��ʼ����ť��   
      jsp=new JScrollPane(jta);   //��ʼ��������壬�����ı���������ڹ�������С�               
      this.setLayout(new FlowLayout());   //���ô��岼��Ϊ��ʽ���֣��˲���Ϊ��������һ����������һ�зŲ�����ת���ڶ��У���               
      this.add(jb);       //����ť�ӵ������С�    
      this.add(jsp);      //�ѹ������ӵ������С�   
      jb.addActionListener(this);     //Ϊ��ťע�ᶯ���¼����������������ťʱ���������¼�������Ϊ����ʵ���˶����¼��������ӿڣ����Ը�����������������      
      this.setBounds(300,300,400,400);    //���ô���߽�ʹ�С��   
      this.setVisible(true);      //���ô���ɼ�������Ĭ���ǲ��ɼ��ģ���     
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //���ô���Ĭ�Ϲرղ�����              
	}
	
	public void actionPerformed(ActionEvent e){   
    	  //ActionListener�ӿ���ķ���������ʵ�֣��������������ť�������ı����»س���Ķ����¼���
    //	  String jfText=jf.getText();     //��ȡ�ı����е����ݡ�
		  
    	  if(ip.length()>0){      //���ı��������ַ������ȴ�����ʱ���������Ϊ0����û�����壩ִ��������䡣
    		  byte[] by=ip.getBytes();        //���ַ�����Ϊ�ֽ����顣                       
    		  try {              
    			  out.write(by);      //���ֽ�����д������������У��ɿͻ��������ա�                
    			  jta.append("��˵��"+ip+"\n");   //������������Ϣ��ʾ���ı����ڡ�                   
    			                
    			  } catch (IOException ex) {       
    				  Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);  //һ���쳣�����������         
    				}       
    	  }   
    }   
    public void run(){  //Runnable�ӿ��еķ���������ʵ�֣����߳̿�����ִ�еĴ��롣      
    	  while(true){    //��ΪҪ���ϵؽ�����Ϣ�������߳�Ҫһֱ���У�������while(true)��        
    		  byte[] by=new byte[1024];   //�������տͻ��˷�������Ϣ�� 
    		  java.util.Arrays.fill(by, (byte) 0);
    		  try {
    			  
    			  int count=in.read(by);      //��������������ȡ���Կͻ��˵���Ϣ�����ض�ȡ����Ч�ֽڸ�����                
    		      jta.setText("�ͻ���˵��"+new String(by,0,count)+"\n");  //���ͻ��˷�������Ϣ��ʾ���ı����С�
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
    		    	  ip="�������뵽һ��ip��ַ�������ظ�����";
    		    	  
    		      }
    		      else if(ippool.isEmpty()) {
    		    	  ip="�޿���ip";
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
    		    	  ip="�޿���ip��ַ";
    		      }*/
    		      } catch (IOException ex) {           
    		    	  Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);  //һ���쳣�����������        
    		        }
    	   }
	}
}


