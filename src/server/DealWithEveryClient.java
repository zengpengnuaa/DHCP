package server;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DealWithEveryClient extends Thread{   
	Socket soc;     //客户端对象，服务器接收到的客户端。
	public DealWithEveryClient(Socket soc){     //构造方法。        
		this.soc=soc;       //传递服务器接收到的客户端套接字。         
		}    
	public void run(){  
		//Thread类中的方法（必须重写），线程开启后执行的代码。      
		try {    
			ServerFrame sf=new ServerFrame(soc);    //创建一个服务器聊天窗口                
			Thread thread=new Thread(sf);       //实例化接收客户端消息的线程对象。          
			thread.start();     //开启该线程。       
			} catch (IOException ex) {      
			Logger.getLogger(DealWithEveryClient.class.getName()).log(Level.SEVERE, null, ex);  
			//一种异常处理，不必深究。       
		}
	}
}
