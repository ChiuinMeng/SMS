package sms.server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.Scanner;

import sms.Config;
import sms.FileTool;
import sms.Security;
import sms.dal.Block;
import sms.dal.BlockChain;
import sms.dal.DatabaseAccess;

public class Server {
	private Scanner sc = new Scanner(System.in);
	private Config config;
	private DatabaseAccess da;
	private boolean firstRun = false;
	private String userhome = System.getProperty("user.home");
	
	Server() throws IOException, ClassNotFoundException{
		//1. �����ļ��������ļ������ڣ�˵���ǵ�һ�����У���Ϊһ�����й������ļ���Ȼ���ɣ�
		File configfile = new File(userhome+"\\.sms\\sms.config");
		if(!configfile.exists()) {//���ļ�������
			firstRun = true;
			System.out.println("�������У����潫¼��һЩ��Ϣ���밴��ʾ����");
			config = new Config();
			System.out.println("�����Ƿ�ΪѧУ��������1:�� 2����");
			int choose = sc.nextInt();
			if(choose==1) {//��ΪѧУ������
				schoolFirstRun();
			}else {//��Ϊ��ѧУ�ķ����������
				//todo
			}
			File p = configfile.getParentFile();
			if(!p.exists()) p.mkdir();
			configfile.createNewFile();//�����˸��ļ����ļ�Ϊ�ա�
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configfile));
			oos.writeObject(config);
			oos.close();
			System.out.println("��Ϣ¼�����");
		}else {//���ļ����ڣ����ظö���
			System.out.println("���������ļ��С�����");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configfile));
			config = (Config)ois.readObject();
			ois.close();
			System.out.println("�����ļ���������ɡ�");
		}
		// 3.�����������ݿ����Ϣ
		da = DatabaseAccess.getInstance();
		da.setDriverName(config.getDatabaseDriverName());
		da.setURL(config.getDatabaseURL());
		da.setUser(config.getDatabaseUserName());
		da.setPassword(config.getDatabasePassword());
		
//		checkIfFirstRun();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Server server = new Server();
		server.start();
	}

	private void start() {
		Monitor monitor = new Monitor();
		monitor.run();
	}
	
	
	/**
	 * ѧУ��������һ������ʱ����ִ�еĲ���
	 */
	private void schoolFirstRun() {
		//1�������Ϣ¼��
		config.setHostType(2);
		System.out.println("������ѧУ���ƣ�");
		sc.nextLine();
		config.setSchoolName(sc.nextLine());
		System.out.println("������ѧУ�������Ķ����ip��ַ");
		config.setServerIP(sc.nextLine());
		System.out.println("������˿ںţ�����1934");
		config.setPort(sc.nextInt());
		System.out.println("������ѧУ���ݿ�����������磺com.mysql.cj.jdbc.Driver");
		sc.nextLine();
		config.setDatabaseDriverName(sc.nextLine());
		System.out.println("������ѧУ���ݿ���ʵ�URL���磺jdbc:mysql://localhost:3306/sms?serverTimezone=GMT");
		config.setDatabaseURL(sc.nextLine());
		System.out.println("������ѧУ���ݿ���ʵ�username");
		config.setDatabaseUserName(sc.nextLine());
		System.out.println("������ѧУ���ݿ���ʵ�����");
		config.setDatabasePassword(sc.nextLine());
		//2. ������Կ˽Կ��
		Security s = new Security();
		s.generateKeyPair();
		FileTool.crateFile(userhome+"\\.sms\\publicKey", s.getPublicKey());
		FileTool.crateFile(userhome+"\\.sms\\privateKey", s.getPrivateKey());
		System.out.println("�����ɹ�Կ˽Կ�ԣ������Ʊ���˽Կ���ļ���ַ��"+userhome+"\\\\.sms");
		config.setLocalPublicKey(s.getRSAPublicKey());
		config.setSchoolPublicKey(s.getRSAPublicKey());
		//3. �����������飬�����Լ�˽Կǩ��
		BlockChain bc = new BlockChain();
		String info = "{\"ѧУ��\":\"��ͷ��ѧ\",\"ѧУ��ʶ��\":4144010560,\"ʡ��\":\"�㶫ʡ\"}";
		Block firstBlock = new Block(1, null, System.currentTimeMillis(),s.getRSAPublicKey(),info);
		firstBlock = firstBlock.generateBlock(s.getRSAPrivateCrtKey());
		bc.createFirstBlock(firstBlock);
		//2. �㲥����������Լ��Ĺ�Կ
		
	}
}