package sms.client;

import java.util.Scanner;

import com.alibaba.fastjson.JSONObject;

import sms.MessageSender;
import sms.Security;

public class LoginProcedure {
	private Scanner sc = new Scanner(System.in);
	private String username;
	private String private_key_path;
	private String ip;
	private int port;
	public final static int USER_STUDENT = 1;
	public final static int USER_TEACHER = 2;
	public final static int USER_SCHOOL = 3;
	public final static int USER_OTHER = 4;
	public final static int LOGIN_FAILED = -1;
	
	public LoginProcedure(String ip,int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 * 使用私钥登录过程，若：<br>
	 * 1. 登录成功，返回用户类型。<br>
	 * 2. 登录失败，返回LOGIN_FAILED.
	 * @return
	 */
	public int login() {
		System.out.println("请输入用户名：");
		username = sc.nextLine();
		
		System.out.println("请输入密钥文件位置，含文件名，如：C:\\Users\\Chiuin\\.sms\\privateKey");
		private_key_path = sc.nextLine();
		
		String currentTime = String.valueOf(System.currentTimeMillis());
		
		JSONObject object = new JSONObject();
		object.put("action", "login");
		object.put("type","privateKey");
		object.put("username",username);
		object.put("currentTime",currentTime);
		object.put("verification",Security.encryptString(currentTime, private_key_path,Security.KeyType_Private));
		String sendinfo = object.toJSONString();//待发送的消息，json格式
		System.out.println("待发送信息内容："+sendinfo);
		
		String re = MessageSender.getInstance().sendAndReceiveMessage(ip, port, sendinfo);
		System.out.println("收到回信内容："+re);
		//根据收到的消息判断结果。
		
		System.out.println("使用私钥登录失败");
		return LOGIN_FAILED;
	}
}
