package server;
import java.io.IOException;
import java.net.*;
public class Server { 
	/**    * @param   args the command line arguments     */   
	private static ServerSocket ss;
	public static void main(String[] args) throws IOException {  
		// TODO code application logic here     
		ss=new ServerSocket(7227);        //�������������׽��֣�ָ���÷���������Ķ˿ڡ�     
		while(true){        //��������£�����������Ҫһֱ���У��Ա���ʱ���ܽ��տͻ��˵����ӡ�         
			Socket soc=null;        //�ͻ����׽��ֶ��󣬳�ʼ��Ϊ�ա�              
			soc=ss.accept();        //���������յ��ͻ������ӣ����ظÿͻ��˶��󣬴˷�����һֱ������ֱ�����յ��ͻ��˵����ӡ�          
			if(soc!=null){      //����ͻ��������ˣ�ִ��������롣                       
				DealWithEveryClient dec=new DealWithEveryClient(soc);   //Ϊÿ���ͻ��˿���һ�����촰�ڡ�   
				dec.start();        //�������̡߳�    
			}
		}
	}
	
}

