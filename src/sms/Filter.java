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
	//处理接受到的socket内容
	Socket socket;
	InetAddress address;

	Filter(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		address = socket.getInetAddress();
		PeerManagement.addHost(address);
        System.out.println("当前客户端的Ip地址："+ address.getHostAddress());
        
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        String info = "";
        try {
        	is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			// 会阻塞！若该io流中没有数据！
			info = br.readLine();
			
			System.out.println("收到消息，说：" + info);
		} catch (IOException e) {
			System.out.println("io流出错");
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
        	// 1. 如果是登录请求
        	if(map.get("action").equals("login")) {
        		if(new Handler(map).login()) {
        			System.out.println("认证成功，正在发回确认消息。");
        			
        			MessageSender.getInstance().sendMessage(socket,"success");
        		}else {
        			System.out.println("认证失败，正在发回失败消息。");
        			MessageSender.getInstance().sendMessage(socket,"failed");
        		}
        	}
        	// 2. 如果是upload，即上传公钥
        	else if(map.get("action").equals("upload")) {
        		System.out.println("收到上传公钥的请求。");
        		if(new Handler(map).uploadPublicKey()) {
        			System.out.println("已更新公钥");
        			MessageSender.getInstance().sendMessage(socket,"success");
        		}else {
        			System.out.println("上传公钥失败。");
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
			System.out.println("关闭资源错误");
			e.printStackTrace();
		}
	}

}
