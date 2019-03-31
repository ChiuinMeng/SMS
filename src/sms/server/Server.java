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
		//1. 加载文件，若该文件不存在，说明是第一次运行（因为一旦运行过，该文件必然生成）
		File configfile = new File(userhome+"\\.sms\\sms.config");
		if(!configfile.exists()) {//若文件不存在
			firstRun = true;
			System.out.println("初次运行，下面将录入一些信息，请按提示操作");
			config = new Config();
			System.out.println("本机是否为学校服务器？1:是 2：否");
			int choose = sc.nextInt();
			if(choose==1) {//若为学校服务器
				schoolFirstRun();
			}else {//若为非学校的服务器主结点
				//todo
			}
			File p = configfile.getParentFile();
			if(!p.exists()) p.mkdir();
			configfile.createNewFile();//创建了该文件，文件为空。
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configfile));
			oos.writeObject(config);
			oos.close();
			System.out.println("信息录入完成");
		}else {//若文件存在，加载该对象
			System.out.println("加载配置文件中。。。");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configfile));
			config = (Config)ois.readObject();
			ois.close();
			System.out.println("配置文件加载已完成。");
		}
		// 3.配置连接数据库的信息
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
	 * 学校服务器第一次运行时，需执行的操作
	 */
	private void schoolFirstRun() {
		//1：相关信息录入
		config.setHostType(2);
		System.out.println("请输入学校名称：");
		sc.nextLine();
		config.setSchoolName(sc.nextLine());
		System.out.println("请输入学校服务器的对外的ip地址");
		config.setServerIP(sc.nextLine());
		System.out.println("请输入端口号，建议1934");
		config.setPort(sc.nextInt());
		System.out.println("请输入学校数据库的驱动名，如：com.mysql.cj.jdbc.Driver");
		sc.nextLine();
		config.setDatabaseDriverName(sc.nextLine());
		System.out.println("请输入学校数据库访问的URL，如：jdbc:mysql://localhost:3306/sms?serverTimezone=GMT");
		config.setDatabaseURL(sc.nextLine());
		System.out.println("请输入学校数据库访问的username");
		config.setDatabaseUserName(sc.nextLine());
		System.out.println("请输入学校数据库访问的密码");
		config.setDatabasePassword(sc.nextLine());
		//2. 创建公钥私钥对
		Security s = new Security();
		s.generateKeyPair();
		FileTool.crateFile(userhome+"\\.sms\\publicKey", s.getPublicKey());
		FileTool.crateFile(userhome+"\\.sms\\privateKey", s.getPrivateKey());
		System.out.println("已生成公钥私钥对，请妥善保管私钥，文件地址："+userhome+"\\\\.sms");
		config.setLocalPublicKey(s.getRSAPublicKey());
		config.setSchoolPublicKey(s.getRSAPublicKey());
		//3. 创建创世区块，并用自己私钥签名
		BlockChain bc = new BlockChain();
		String info = "{\"学校名\":\"汕头大学\",\"学校标识码\":4144010560,\"省份\":\"广东省\"}";
		Block firstBlock = new Block(1, null, System.currentTimeMillis(),s.getRSAPublicKey(),info);
		firstBlock = firstBlock.generateBlock(s.getRSAPrivateCrtKey());
		bc.createFirstBlock(firstBlock);
		//2. 广播创世区块和自己的公钥
		
	}
}