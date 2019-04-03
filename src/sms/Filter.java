package sms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Filter implements Runnable{
	//������ܵ���socket����
	Socket socket;
	InetAddress address;

	Filter(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		address = socket.getInetAddress();
		PeerManagement.addHost(address);
        System.out.println("��ǰ�ͻ��˵�Ip��ַ��"+ address.getHostAddress());
        
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        String info = "";
        try {
        	is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			// ������������io����û�����ݣ�
			info = br.readLine();
			
			System.out.println("�յ���Ϣ��˵��" + info);
		} catch (IOException e) {
			System.out.println("io������");
			e.printStackTrace();
		}
        int index = info.indexOf("?");
        Map<String,String> map = new HashMap<>();
        if(index!=-1) {
        	String param = info.substring(index+1);
            String[] params = param.split("&");
            for (String item:params) {
                String[] kv = item.split("=");
                map.put(kv[0],kv[1]);
            }
        }
        System.out.println("map = "+map);
        if(map.containsKey("action")) {
        	// 1. ����ǵ�¼����
        	if(map.get("action").equals("login")) {
        		if(new Handler(map).login()) {
        			System.out.println("��֤�ɹ������ڷ���ȷ����Ϣ��");
        			
        			MessageSender.getInstance().sendMessage(socket,"success");
        		}else {
        			System.out.println("��֤ʧ�ܣ����ڷ���ʧ����Ϣ��");
        			MessageSender.getInstance().sendMessage(socket,"failed");
        		}
        	}
        	// 2. �����upload�����ϴ���Կ
        	else if(map.get("action").equals("upload")) {
        		System.out.println("�յ��ϴ���Կ������");
        		if(new Handler(map).uploadPublicKey()) {
        			System.out.println("�Ѹ��¹�Կ");
        			MessageSender.getInstance().sendMessage(socket,"success");
        		}else {
        			System.out.println("�ϴ���Կʧ�ܡ�");
        			MessageSender.getInstance().sendMessage(socket,"failed");
        		}
        	}
        	else {
        		System.out.println("action="+map.get("action"));
        	}
        }
        try {
			br.close();
			isr.close();
			is.close();
			socket.close();//
		} catch (IOException e) {
			System.out.println("�ر���Դ����");
			e.printStackTrace();
		}
	}

}
