package sms;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor extends Thread{
	//监听端口1934，处理不同的请求
	ServerSocket serverSocket;
	private int port = 1934;
	
	@Override
	public void run() {
		System.out.println("端口"+getPort()+"监听准备中。。。");
		try {
			serverSocket = new ServerSocket(getPort());
			System.out.println("端口"+getPort()+"监听正在监听中。。。");
			while(true) {
				//会阻塞!，直到客户端创建一个socket!
				Socket socket = serverSocket.accept();
				
				Filter filter = new Filter(socket);
				filter.run(); //将该socket交给filter处理
			}
		} catch (IOException e) {
			System.out.println("监听准备失败");
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
