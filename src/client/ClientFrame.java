package client;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.lang.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ClientFrame extends JFrame implements ActionListener,Runnable{ //客户端聊天窗口，实现两个接口，作为动作事件侦听器和线程任务类。       
	Socket soc; //客户端套接字。     
	JTextField jf;  //文本框。     
	JTextArea jta;  //文本区域。      
	JButton jb; //按钮。 
	JButton jb1;
	JScrollPane jsp;    //滚动面板。      
	InputStream in;     //输入流，用来指向Socket方法获取的网络输入流
	OutputStream out;   //输出流，用来指向Socket方法获取的网络输出流。 
	String ip;
	String ran1;
	public ClientFrame() throws IOException{    //构造方法，用来初始化对象以及做一些设置。 
		super("客户端聊天框");      //调用超类JFrame的构造方法设置聊天框的标题。         
		
		soc=new Socket("127.0.0.1",7227);  //实例化客户端套接字，指定要连接的服务器程序的IP和端口。  
		in=soc.getInputStream();    //获取Socket的输入流。           
		out=soc.getOutputStream();  //获取Socket的输出流。              
		jf=new JTextField(20);      //初始化化文本框，设置容量为20个字符。         
		jta=new JTextArea(20,20);   //初始化文本区域并设置其为20行和20列（一个字符代表一列）。  
		jb=new JButton("发送");     //初始化按钮。          
		jb1=new JButton("断开");
		jsp=new JScrollPane(jta);   //初始化滚动面板，并把文本区域放置在滚动面板中。        
		this.setLayout(new FlowLayout());   //设置窗体布局为流式布局（此布局为从左向右一次添加组件，一行放不下了转到第二行）。  
		this.add(jf);   //将文本框加到窗体中。            
		this.add(jb);   //将按钮加到窗体中。         
		this.add(jb1);
		this.add(jsp);  //把滚动面板加到窗体中。          
		jb.addActionListener(this); //为按钮注册动作事件侦听器（当点击按钮时触发动作事件），因为该类实现了动作事件侦听器接口，所以该类对象就是侦听器。     
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){     //ActionListener接口里的方法，必须实现，用来处理当点击按钮或者在文本框按下回车后的动作事件。            
				try {         
					String bye="exit";
					byte[] by=bye.getBytes();
					out.write(by);  //将字节数组写入网络输出流中，由服务器来接收。
					jta.append("断开与服务器的连接\n");   //将客户端的消息显示在文本区内。    
					soc.close();
					soc=null;
					System.exit(1);
					} catch (IOException ex) {            
						Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);//一种异常处理，不必深究。    
					  }    
		} 
		});
		jf.addActionListener(this); //为文本框注册动作事件侦听器，当按下回车触发动作事件。          
		this.setBounds(300,300,400,400);    //设置窗体边界和大小。            
		this.setVisible(true);      //设置窗体可见（窗体默认是不可见的）。            
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置窗体默认关闭操作。    
		}    
	public void actionPerformed(ActionEvent e){     //ActionListener接口里的方法，必须实现，用来处理当点击按钮或者在文本框按下回车后的动作事件。            
		//	String jfText=jf.getText(); //获取文本框中的内容。
		String jfText="00-06-22-34-02-c5-";
		
		
		jfText=jfText+ran1;
			if(jfText.length()>0){  //当文本框里面字符串长度大于零时（如果长度为0，则没有意义）执行下面语句。         
				byte[] by=jfText.getBytes();    //将字符串变为字节数组。                  
				try {          
					out.write(by);  //将字节数组写入网络输出流中，由服务器来接收。              
					jta.append("我的mac地址是："+jfText+"\n");   //将客户端的消息显示在文本区内。           
					jf.setText("");     //发送完消息后，清空文本框（以便下次输入）。              
					} catch (IOException ex) {            
						Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);//一种异常处理，不必深究。    
					  }      
			}   
	}   
	
  
	public void run(){  //Runnable接口中的方法（必须实现），线程开启后执行的代码。   
		Random r=new Random();
		int ran=r.nextInt(100);
		ran1=String.valueOf(ran);
			while(true){        //因为要不断地接收消息，所以线程要一直运行，所以用while(true)。          
				byte[] b=new byte[1024];    //用来接收服务器发来的消息。              
				try {   
					
					int count=in.read(b);   //用网络输入流读取来自服务器的消息，返回读取的有效字节个数。 
					ip=new String (b,0,count);
				    jta.append("服务器说："+new String(b,0,count)+"\n");    //将服务器发来的消息显示在文本区中。   	 
				} catch (IOException ex) {         
					Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);  //一种异常处理，不必深究。 
					
				 }
			}
	}
}

