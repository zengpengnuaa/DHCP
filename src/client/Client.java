package client;
import java.io.IOException;
public class Client {   
	/**     * @param args the command line arguments     */   
	public static void main(String[] args) throws IOException {     
		// TODO code application logic here     
		ClientFrame cf=new ClientFrame();   //ʵ�������ڶ���ͬʱ��Ҳ���߳��������    
		Thread reciver=new Thread(cf);      //���߳�������󴴽�һ���̡߳�    
		reciver.start();        //�����̣߳��������Է���������Ϣ�� 
		}   
	

}

