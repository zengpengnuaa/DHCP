package server;
import java.io.IOException;
import java.net.*;
public class Server { 
	/**    * @param   args the command line arguments     */   
	private static ServerSocket ss;
	public static void main(String[] args) throws IOException {  
		// TODO code application logic here     
		ss=new ServerSocket(7227);        //创建服务器端套接字，指定该服务器程序的端口。     
		while(true){        //正常情况下，服务器程序要一直运行，以便随时都能接收客户端的连接。         
			Socket soc=null;        //客户端套接字对象，初始化为空。              
			soc=ss.accept();        //服务器接收到客户端连接，返回该客户端对象，此方法会一直阻塞，直到接收到客户端的连接。          
			if(soc!=null){      //如果客户端连接了，执行下面代码。                       
				DealWithEveryClient dec=new DealWithEveryClient(soc);   //为每个客户端开启一个聊天窗口。   
				dec.start();        //开启此线程。    
			}
		}
	}
	
}

