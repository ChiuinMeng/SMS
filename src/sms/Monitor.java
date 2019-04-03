package sms;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor extends Thread{
	//�����˿�1934������ͬ������
	ServerSocket serverSocket;
	private int port = 1934;
	
	@Override
	public void run() {
		System.out.println("�˿�"+getPort()+"����׼���С�����");
		try {
			serverSocket = new ServerSocket(getPort());
			System.out.println("�˿�"+getPort()+"�������ڼ����С�����");
			while(true) {
				//������!��ֱ���ͻ��˴���һ��socket!
				Socket socket = serverSocket.accept();
				
				Filter filter = new Filter(socket);
				filter.run(); //����socket����filter����
			}
		} catch (IOException e) {
			System.out.println("����׼��ʧ��");
			e.printStackTrace();
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}
}
