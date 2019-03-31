package sms.client;
import java.util.Scanner;

import sms.FileTool;
import sms.MessageSender;
import sms.Password;
import sms.Security;

public class Client {
	Scanner sc = new Scanner(System.in);
	int choose;
	String ip = "127.0.0.1";
	int port = 1934;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

	private void start() {
		System.out.println("欢迎使用基于区块链的成绩管理系统：\n1：登录\n2：注册");
		choose = sc.nextInt();
		if(choose==1) login();
		else if(choose==2)register();
		else return;
	}

	private void login() {
		System.out.println("请输入用户名：");
		sc.nextLine();
		String username = sc.nextLine();
		System.out.println("请输入密钥文件位置：");
		String private_key_path = sc.nextLine();
		if(true) { // todo：验证用户名和私钥
			int user_type = 1;//默认0
			if(user_type==1) { //若用户类型为老师
				System.out.println("欢迎：\n1：查询成绩\n2：录入成绩\n3：修改成绩");
				choose = sc.nextInt();
				if(choose==1) { //查询成绩
					System.out.println("请输入录入成绩文件（json格式）位置：");
					String score_file_path = sc.nextLine();
					System.out.println("上传中。。。");
					if(true) {
						System.out.println("上传成功。\n等待确认");
						if(true) {//todo：该过程就复杂了。
							System.out.println("已成功录入成绩");
						}
					}
					else {
						
					}
				}
				else if(choose==2) { //录入成绩
					
				}
				else if(choose==3) { //修改成绩
					
				}
				else {
					
				}
			}
			else if(user_type==2) {
				
			}
			else if(user_type==3) {
				
			}
			else {
				
			}
		}
		else {
			
		}
	}
	
	private void register() {
		System.out.println("请输入用户名：");
		String username = sc.nextLine();
		username = sc.nextLine();
		System.out.println("用户:"+username);
		System.out.println("请输入密码：");
		String password = Password.getEncryptedPassword(); //SHA256加密
		if(Login.getInstance().login(username,password)) {  // todo:若验证成功，要验证用户名和密码。
			System.out.println("请确认个人信息是否正确。\n1：确认\n2：有误");
			choose = sc.nextInt();
			if(choose==1) {
				System.out.println("信息确认成功，现为您生成公钥/私钥对，稍后公钥将上传至服务器(我们不会获取您的私钥)。。。。。");
				Security s = new Security();
				if(s.generateKeyPair()) { // todo:若成功生成公钥/私钥对
					String pubk = s.getPublicKey();
					String prik = s.getPrivateKey();
					String path = System.getProperty("user.home")+"\\.sms\\";
					System.out.println("公钥/私钥对已成功生成，文件位置为："+path);
					FileTool.crateFile(path+"privateKey", pubk);
					System.out.println("公钥为："+pubk);
					FileTool.crateFile(path+"publicKey", pubk);
					System.out.println("私钥为："+prik);
					System.out.println("注意！！！：请妥善保管自己的私钥，切勿乱复制、上传、分享自己的私钥。");
					String message = "?action=upload&username="+username+"&PublicKey="+pubk;
					String r = MessageSender.getInstance().sendAndReceiveMessage(ip, port, message);
					if(r.equals("received")) {
						System.out.println("公钥已成功上传至服务器。注册成功。");
					}
					else {
						System.out.println("返回消息为："+r);
					}
				}
				else {
					
				}
			}
			else if(choose==2) {
				
			}else {
				return ;
			}
		}
	}
}
