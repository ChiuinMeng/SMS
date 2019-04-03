package sms;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageSender {
	private static MessageSender messageSender;
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	private BufferedReader buf;
	
	public static synchronized MessageSender getInstance() {
		if(messageSender==null) return new MessageSender();
		return messageSender;
	}
	
	/**
	 * 发送一条消息给指点主机端口，不用等待该主机回复，数据包在传输过程中可能丢失。
	 * 采用数据报，即UDP协议。
	 * @param ip IP地址
	 * @param port 端口号
	 * @param message 发送消息
	 * @return 
	 */
	public boolean sendUDPMessage(String ip,int port,String message) {
		//todo：待改。
//		try {
//			socket = new Socket(ip, port);//IP错误异常，服务端未监听该端口异常
//			os = socket.getOutputStream();
//			osw = new OutputStreamWriter(os);//转化为字符流
//            bw = new BufferedWriter(osw);//创建缓冲流
//            bw.write(message);
//            bw.flush();//?
//
//            bw.close();
//            osw.close();
//            os.close();
//            socket.close();
//            return true;
//		} catch (UnknownHostException e) {
//			System.out.println("未知主机！");
//			e.printStackTrace();
//			
//		} catch (IOException e) {
//			System.out.println("IO异常！");
//			e.printStackTrace();
//		}
		return false;
	}
	
	/**
	 * 
	 * @param socket
	 * @param message
	 */
	public void sendMessage(Socket socket,String message) {
		try {
			OutputStream os = socket.getOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(message);
            bw.flush();//?
            socket.shutdownOutput();// 关闭输出流
		} catch (IOException e) {
			e.printStackTrace();
		}  
        System.out.println("成功发送消息:"+message);
	}
	
	/**
	 * 发送给指定ip:port一条消息，并接受返回的消息。
	 * 该函数一开始会创建一个新的socket，最后会关掉该socket。
	 * @param ip ip地址
	 * @param port 端口号
	 * @param message 发送给指定主机的消息
	 * @return 指定主机返回的消息
	 */
	public String sendAndReceiveMessage(String ip,int port,String message) {
		try {
			System.out.println("ip:"+ip+"; port:"+port+"; message："+message);
			Socket socket = new Socket(ip, port);//可能存在：IP错误异常，服务端未监听该端口异常
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message);
            bw.flush();//?
            socket.shutdownOutput();// 关闭输出流

            buf =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String echo = buf.readLine();
            System.out.println("返回消息："+echo);
            
            //关闭占有资源
            bw.close();
            buf.close();
            socket.close();
            return echo;
		} catch (UnknownHostException e) {
			System.out.println("未知主机！");
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("IO异常！");
			e.printStackTrace();
		}
		return null;
	}
}
