package sms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;

import sms.dal.Block;
import sms.dal.BlockChain;
import sms.dal.DatabaseAccess;

/**
 * 后台运行的服务！不会涉及与用户的交互
 * @author Chiuin
 *
 */
public class SMS {
	public final static String userhome = System.getProperty("user.home");
	public final static String operateSystem = System.getProperty("os.name").toLowerCase();
	public static String path;
	public static Config config;
	static PeerManagement pm;
	private static DatabaseAccess da;
	
	static {
		if(operateSystem.contains("linux")) {
			path = userhome+"/.sms/";
		}else if(operateSystem.contains("windows")) {
			path = userhome+"\\.sms\\";
		}else {
			path = userhome;//其它操作系统，苹果等，不清楚其文件系统命名规则
		}
	}

	public static void main(String[] args) {
		/**
		 * 加载config.json配置文件，初始化config对象。
		 * 若该文件不存在，输出错误信息，请求创建，退出。
		 */
		JSONObject config_json = JSONObject.parseObject(FileTool.readFileToString(path+"config.json"));
		config = new Config(config_json);
		
		if(config.getHostType().contains("学校")) {
			/**
			 * 若为学校主机，准备连接数据库的工作。配置数据库的信息,url等
			 * 若连接失败，报错，请求修改config.json文件，退出
			 */
			da = DatabaseAccess.getInstance();
			da.setDriverName(config.getDatabase().getDriverName());
			da.setURL(config.getDatabase().getUrl());
			da.setUser(config.getDatabase().getUserName());
			da.setPassword(config.getDatabase().getPassword());
			
			if(!config.isHadRunOnce()) {
				/**
				 * 若为学校主机，且第一次运行，
				 * 1. 尝试创建公私钥对文件。若该文件已存在，不会创建。
				 * 2. 创建创世区块
				 */
				if(!new File(path+"privateKey").exists() || !new File(path+"publicKey").exists()) {
					System.out.println("准备生成公私钥对");
					Security s = new Security();
					s.generateKeyPair();
					String publicKey = s.getPublicKey();
					String privateKey = s.getPrivateKey();
					FileTool.crateFile(path+"publicKey", publicKey);
					FileTool.crateFile(path+"privateKey", privateKey);
					config.getKey().setLocalPublicKey(publicKey);
					config.getKey().setSchoolPublicKey(publicKey);
					
					//创建创世区块
					BlockChain bc = BlockChain.getBlockChain();
					String info = "{\"学校名\":\"汕头大学\",\"学校标识码\":4144010560,\"省份\":\"广东省\"}";
					Block firstBlock = new Block(1, null, System.currentTimeMillis(),s.getPublicKey(),info);
					firstBlock = firstBlock.generateBlock(s.getRSAPrivateCrtKey());
					bc.add(firstBlock);
//					System.out.println(firstBlock.toJSONString());
				}
				
				//设置学校为非第一次运行。
				config.setHadRunOnce(true);
				try {
					FileUtils.writeStringToFile(new File(path+"config.json"),config.toJSONStringFormat(),"UTF8");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * 若为学校主机，开启tcp管理，监听并处理所有的tcp消息。
			 * 1. 用户名/密码登录请求
			 * 2. 用户名/私钥登录请求
			 * 3. 查询请求
			 * 4. 老师上传成绩请求->验证->签名->创建区块->udp广播
			 */
			Monitor monitor = new Monitor();
			monitor.start();
		}
		
		/**
		 * 开启udp网络管理，监听并处理所有的udp消息。
		 * 1. 收到创建区块的信息->验证信息->创建区块->持久化->广播
		 * 2. 新入区块链->广播->收到消息->修改ip池
		 */
		pm = PeerManagement.getInstance(config.getPort());
		pm.listenUDPMessage();
		pm.handleUDPMessage();
		pm.maintainIpPool();
		
		System.out.println("sms running......");
	}

}
