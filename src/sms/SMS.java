package sms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;

import sms.dal.Block;
import sms.dal.BlockChain;
import sms.dal.DatabaseAccess;

/**
 * ��̨���еķ��񣡲����漰���û��Ľ���
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
			path = userhome;//��������ϵͳ��ƻ���ȣ���������ļ�ϵͳ��������
		}
	}

	public static void main(String[] args) {
		/**
		 * ����config.json�����ļ�����ʼ��config����
		 * �����ļ������ڣ����������Ϣ�����󴴽����˳���
		 */
		JSONObject config_json = JSONObject.parseObject(FileTool.readFileToString(path+"config.json"));
		config = new Config(config_json);
		
		if(config.getHostType().contains("ѧУ")) {
			/**
			 * ��ΪѧУ������׼���������ݿ�Ĺ������������ݿ����Ϣ,url��
			 * ������ʧ�ܣ����������޸�config.json�ļ����˳�
			 */
			da = DatabaseAccess.getInstance();
			da.setDriverName(config.getDatabase().getDriverName());
			da.setURL(config.getDatabase().getUrl());
			da.setUser(config.getDatabase().getUserName());
			da.setPassword(config.getDatabase().getPassword());
			
			if(!config.isHadRunOnce()) {
				/**
				 * ��ΪѧУ�������ҵ�һ�����У�
				 * 1. ���Դ�����˽Կ���ļ��������ļ��Ѵ��ڣ����ᴴ����
				 * 2. ������������
				 */
				if(!new File(path+"privateKey").exists() || !new File(path+"publicKey").exists()) {
					System.out.println("׼�����ɹ�˽Կ��");
					Security s = new Security();
					s.generateKeyPair();
					String publicKey = s.getPublicKey();
					String privateKey = s.getPrivateKey();
					FileTool.crateFile(path+"publicKey", publicKey);
					FileTool.crateFile(path+"privateKey", privateKey);
					config.getKey().setLocalPublicKey(publicKey);
					config.getKey().setSchoolPublicKey(publicKey);
					
					//������������
					BlockChain bc = BlockChain.getBlockChain();
					String info = "{\"ѧУ��\":\"��ͷ��ѧ\",\"ѧУ��ʶ��\":4144010560,\"ʡ��\":\"�㶫ʡ\"}";
					Block firstBlock = new Block(1, null, System.currentTimeMillis(),s.getPublicKey(),info);
					firstBlock = firstBlock.generateBlock(s.getRSAPrivateCrtKey());
					bc.add(firstBlock);
//					System.out.println(firstBlock.toJSONString());
				}
				
				//����ѧУΪ�ǵ�һ�����С�
				config.setHadRunOnce(true);
				try {
					FileUtils.writeStringToFile(new File(path+"config.json"),config.toJSONStringFormat(),"UTF8");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * ��ΪѧУ����������tcp�����������������е�tcp��Ϣ��
			 * 1. �û���/�����¼����
			 * 2. �û���/˽Կ��¼����
			 * 3. ��ѯ����
			 * 4. ��ʦ�ϴ��ɼ�����->��֤->ǩ��->��������->udp�㲥
			 */
			Monitor monitor = new Monitor();
			monitor.start();
		}
		
		/**
		 * ����udp��������������������е�udp��Ϣ��
		 * 1. �յ������������Ϣ->��֤��Ϣ->��������->�־û�->�㲥
		 * 2. ����������->�㲥->�յ���Ϣ->�޸�ip��
		 */
		pm = PeerManagement.getInstance(config.getPort());
		pm.listenUDPMessage();
		pm.handleUDPMessage();
		pm.maintainIpPool();
		
		System.out.println("sms running......");
	}

}
