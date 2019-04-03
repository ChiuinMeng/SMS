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
	 * ����һ����Ϣ��ָ�������˿ڣ����õȴ��������ظ������ݰ��ڴ�������п��ܶ�ʧ��
	 * �������ݱ�����UDPЭ�顣
	 * @param ip IP��ַ
	 * @param port �˿ں�
	 * @param message ������Ϣ
	 * @return 
	 */
	public boolean sendUDPMessage(String ip,int port,String message) {
		//todo�����ġ�
//		try {
//			socket = new Socket(ip, port);//IP�����쳣�������δ�����ö˿��쳣
//			os = socket.getOutputStream();
//			osw = new OutputStreamWriter(os);//ת��Ϊ�ַ���
//            bw = new BufferedWriter(osw);//����������
//            bw.write(message);
//            bw.flush();//?
//
//            bw.close();
//            osw.close();
//            os.close();
//            socket.close();
//            return true;
//		} catch (UnknownHostException e) {
//			System.out.println("δ֪������");
//			e.printStackTrace();
//			
//		} catch (IOException e) {
//			System.out.println("IO�쳣��");
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
            socket.shutdownOutput();// �ر������
		} catch (IOException e) {
			e.printStackTrace();
		}  
        System.out.println("�ɹ�������Ϣ:"+message);
	}
	
	/**
	 * ���͸�ָ��ip:portһ����Ϣ�������ܷ��ص���Ϣ��
	 * �ú���һ��ʼ�ᴴ��һ���µ�socket������ص���socket��
	 * @param ip ip��ַ
	 * @param port �˿ں�
	 * @param message ���͸�ָ����������Ϣ
	 * @return ָ���������ص���Ϣ
	 */
	public String sendAndReceiveMessage(String ip,int port,String message) {
		try {
			System.out.println("ip:"+ip+"; port:"+port+"; message��"+message);
			Socket socket = new Socket(ip, port);//���ܴ��ڣ�IP�����쳣�������δ�����ö˿��쳣
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message);
            bw.flush();//?
            socket.shutdownOutput();// �ر������

            buf =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String echo = buf.readLine();
            System.out.println("������Ϣ��"+echo);
            
            //�ر�ռ����Դ
            bw.close();
            buf.close();
            socket.close();
            return echo;
		} catch (UnknownHostException e) {
			System.out.println("δ֪������");
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("IO�쳣��");
			e.printStackTrace();
		}
		return null;
	}
}
