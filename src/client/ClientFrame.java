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

public class ClientFrame extends JFrame implements ActionListener,Runnable{ //�ͻ������촰�ڣ�ʵ�������ӿڣ���Ϊ�����¼����������߳������ࡣ       
	Socket soc; //�ͻ����׽��֡�     
	JTextField jf;  //�ı���     
	JTextArea jta;  //�ı�����      
	JButton jb; //��ť�� 
	JButton jb1;
	JScrollPane jsp;    //������塣      
	InputStream in;     //������������ָ��Socket������ȡ������������
	OutputStream out;   //�����������ָ��Socket������ȡ������������� 
	String ip;
	String ran1;
	public ClientFrame() throws IOException{    //���췽����������ʼ�������Լ���һЩ���á� 
		super("�ͻ��������");      //���ó���JFrame�Ĺ��췽�����������ı��⡣         
		
		soc=new Socket("127.0.0.1",7227);  //ʵ�����ͻ����׽��֣�ָ��Ҫ���ӵķ����������IP�Ͷ˿ڡ�  
		in=soc.getInputStream();    //��ȡSocket����������           
		out=soc.getOutputStream();  //��ȡSocket���������              
		jf=new JTextField(20);      //��ʼ�����ı�����������Ϊ20���ַ���         
		jta=new JTextArea(20,20);   //��ʼ���ı�����������Ϊ20�к�20�У�һ���ַ�����һ�У���  
		jb=new JButton("����");     //��ʼ����ť��          
		jb1=new JButton("�Ͽ�");
		jsp=new JScrollPane(jta);   //��ʼ��������壬�����ı���������ڹ�������С�        
		this.setLayout(new FlowLayout());   //���ô��岼��Ϊ��ʽ���֣��˲���Ϊ��������һ����������һ�зŲ�����ת���ڶ��У���  
		this.add(jf);   //���ı���ӵ������С�            
		this.add(jb);   //����ť�ӵ������С�         
		this.add(jb1);
		this.add(jsp);  //�ѹ������ӵ������С�          
		jb.addActionListener(this); //Ϊ��ťע�ᶯ���¼����������������ťʱ���������¼�������Ϊ����ʵ���˶����¼��������ӿڣ����Ը�����������������     
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){     //ActionListener�ӿ���ķ���������ʵ�֣��������������ť�������ı����»س���Ķ����¼���            
				try {         
					String bye="exit";
					byte[] by=bye.getBytes();
					out.write(by);  //���ֽ�����д������������У��ɷ����������ա�
					jta.append("�Ͽ��������������\n");   //���ͻ��˵���Ϣ��ʾ���ı����ڡ�    
					soc.close();
					soc=null;
					System.exit(1);
					} catch (IOException ex) {            
						Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);//һ���쳣�����������    
					  }    
		} 
		});
		jf.addActionListener(this); //Ϊ�ı���ע�ᶯ���¼��������������»س����������¼���          
		this.setBounds(300,300,400,400);    //���ô���߽�ʹ�С��            
		this.setVisible(true);      //���ô���ɼ�������Ĭ���ǲ��ɼ��ģ���            
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //���ô���Ĭ�Ϲرղ�����    
		}    
	public void actionPerformed(ActionEvent e){     //ActionListener�ӿ���ķ���������ʵ�֣��������������ť�������ı����»س���Ķ����¼���            
		//	String jfText=jf.getText(); //��ȡ�ı����е����ݡ�
		String jfText="00-06-22-34-02-c5-";
		
		
		jfText=jfText+ran1;
			if(jfText.length()>0){  //���ı��������ַ������ȴ�����ʱ���������Ϊ0����û�����壩ִ��������䡣         
				byte[] by=jfText.getBytes();    //���ַ�����Ϊ�ֽ����顣                  
				try {          
					out.write(by);  //���ֽ�����д������������У��ɷ����������ա�              
					jta.append("�ҵ�mac��ַ�ǣ�"+jfText+"\n");   //���ͻ��˵���Ϣ��ʾ���ı����ڡ�           
					jf.setText("");     //��������Ϣ������ı����Ա��´����룩��              
					} catch (IOException ex) {            
						Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);//һ���쳣�����������    
					  }      
			}   
	}   
	
  
	public void run(){  //Runnable�ӿ��еķ���������ʵ�֣����߳̿�����ִ�еĴ��롣   
		Random r=new Random();
		int ran=r.nextInt(100);
		ran1=String.valueOf(ran);
			while(true){        //��ΪҪ���ϵؽ�����Ϣ�������߳�Ҫһֱ���У�������while(true)��          
				byte[] b=new byte[1024];    //�������շ�������������Ϣ��              
				try {   
					
					int count=in.read(b);   //��������������ȡ���Է���������Ϣ�����ض�ȡ����Ч�ֽڸ����� 
					ip=new String (b,0,count);
				    jta.append("������˵��"+new String(b,0,count)+"\n");    //����������������Ϣ��ʾ���ı����С�   	 
				} catch (IOException ex) {         
					Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);  //һ���쳣����������� 
					
				 }
			}
	}
}

