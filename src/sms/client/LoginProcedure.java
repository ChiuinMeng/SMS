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
	 * ʹ��˽Կ��¼���̣�����<br>
	 * 1. ��¼�ɹ��������û����͡�<br>
	 * 2. ��¼ʧ�ܣ�����LOGIN_FAILED.
	 * @return
	 */
	public int login() {
		System.out.println("�������û�����");
		username = sc.nextLine();
		
		System.out.println("��������Կ�ļ�λ�ã����ļ������磺C:\\Users\\Chiuin\\.sms\\privateKey");
		private_key_path = sc.nextLine();
		
		String currentTime = String.valueOf(System.currentTimeMillis());
		
		JSONObject object = new JSONObject();
		object.put("action", "login");
		object.put("type","privateKey");
		object.put("username",username);
		object.put("currentTime",currentTime);
		object.put("verification",Security.encryptString(currentTime, private_key_path,Security.KeyType_Private));
		String sendinfo = object.toJSONString();//�����͵���Ϣ��json��ʽ
		System.out.println("��������Ϣ���ݣ�"+sendinfo);
		
		String re = MessageSender.getInstance().sendAndReceiveMessage(ip, port, sendinfo);
		System.out.println("�յ��������ݣ�"+re);
		//�����յ�����Ϣ�жϽ����
		
		System.out.println("ʹ��˽Կ��¼ʧ��");
		return LOGIN_FAILED;
	}
}
