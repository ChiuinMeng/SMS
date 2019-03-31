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
		System.out.println("��ӭʹ�û����������ĳɼ�����ϵͳ��\n1����¼\n2��ע��");
		choose = sc.nextInt();
		if(choose==1) login();
		else if(choose==2)register();
		else return;
	}

	private void login() {
		System.out.println("�������û�����");
		sc.nextLine();
		String username = sc.nextLine();
		System.out.println("��������Կ�ļ�λ�ã�");
		String private_key_path = sc.nextLine();
		if(true) { // todo����֤�û�����˽Կ
			int user_type = 1;//Ĭ��0
			if(user_type==1) { //���û�����Ϊ��ʦ
				System.out.println("��ӭ��\n1����ѯ�ɼ�\n2��¼��ɼ�\n3���޸ĳɼ�");
				choose = sc.nextInt();
				if(choose==1) { //��ѯ�ɼ�
					System.out.println("������¼��ɼ��ļ���json��ʽ��λ�ã�");
					String score_file_path = sc.nextLine();
					System.out.println("�ϴ��С�����");
					if(true) {
						System.out.println("�ϴ��ɹ���\n�ȴ�ȷ��");
						if(true) {//todo���ù��̾͸����ˡ�
							System.out.println("�ѳɹ�¼��ɼ�");
						}
					}
					else {
						
					}
				}
				else if(choose==2) { //¼��ɼ�
					
				}
				else if(choose==3) { //�޸ĳɼ�
					
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
		System.out.println("�������û�����");
		String username = sc.nextLine();
		username = sc.nextLine();
		System.out.println("�û�:"+username);
		System.out.println("���������룺");
		String password = Password.getEncryptedPassword(); //SHA256����
		if(Login.getInstance().login(username,password)) {  // todo:����֤�ɹ���Ҫ��֤�û��������롣
			System.out.println("��ȷ�ϸ�����Ϣ�Ƿ���ȷ��\n1��ȷ��\n2������");
			choose = sc.nextInt();
			if(choose==1) {
				System.out.println("��Ϣȷ�ϳɹ�����Ϊ�����ɹ�Կ/˽Կ�ԣ��Ժ�Կ���ϴ���������(���ǲ����ȡ����˽Կ)����������");
				Security s = new Security();
				if(s.generateKeyPair()) { // todo:���ɹ����ɹ�Կ/˽Կ��
					String pubk = s.getPublicKey();
					String prik = s.getPrivateKey();
					String path = System.getProperty("user.home")+"\\.sms\\";
					System.out.println("��Կ/˽Կ���ѳɹ����ɣ��ļ�λ��Ϊ��"+path);
					FileTool.crateFile(path+"privateKey", pubk);
					System.out.println("��ԿΪ��"+pubk);
					FileTool.crateFile(path+"publicKey", pubk);
					System.out.println("˽ԿΪ��"+prik);
					System.out.println("ע�⣡�����������Ʊ����Լ���˽Կ�������Ҹ��ơ��ϴ��������Լ���˽Կ��");
					String message = "?action=upload&username="+username+"&PublicKey="+pubk;
					String r = MessageSender.getInstance().sendAndReceiveMessage(ip, port, message);
					if(r.equals("received")) {
						System.out.println("��Կ�ѳɹ��ϴ�����������ע��ɹ���");
					}
					else {
						System.out.println("������ϢΪ��"+r);
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
