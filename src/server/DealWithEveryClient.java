package server;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DealWithEveryClient extends Thread{   
	Socket soc;     //�ͻ��˶��󣬷��������յ��Ŀͻ��ˡ�
	public DealWithEveryClient(Socket soc){     //���췽����        
		this.soc=soc;       //���ݷ��������յ��Ŀͻ����׽��֡�         
		}    
	public void run(){  
		//Thread���еķ�����������д�����߳̿�����ִ�еĴ��롣      
		try {    
			ServerFrame sf=new ServerFrame(soc);    //����һ�����������촰��                
			Thread thread=new Thread(sf);       //ʵ�������տͻ�����Ϣ���̶߳���          
			thread.start();     //�������̡߳�       
			} catch (IOException ex) {      
			Logger.getLogger(DealWithEveryClient.class.getName()).log(Level.SEVERE, null, ex);  
			//һ���쳣�����������       
		}
	}
}
