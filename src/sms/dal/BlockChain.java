package sms.dal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import sms.PeerManagement;
import sms.SMS;

public class BlockChain {
	private static BlockChain blockChain;
	Deque<Block> chain = new LinkedList<Block>();
	
	
	private String schoolPublicKey = SMS.config.getKey().getSchoolPublicKey();
	
	private BlockChain() {
	}
	public static synchronized BlockChain getBlockChain() {
		if(blockChain==null) blockChain = new BlockChain();
		return blockChain;
	}

	public void add(Block block) {
		/**
		 * 1. ��֤���飬�ж��Ƿ���Ȩ��add
		 * 2. �ڴ��������
		 * 3. ����ͬ������
		 * 4. �㲥
		 */
		//1.����Ϻ��淶&&������ΪѧУ
		if(Block.checkBlock(block) && block.getCreatorPublicKey().equals(schoolPublicKey)) {
			//2. �ڴ��������
			chain.add(block);
			//3. ����ͬ������
			synchronousBlock(block);
			//4. ���������Ҫ�㲥������
			PeerManagement.broadcastUDPMessage(block.getInfo(),SMS.config.getPort());
		}
	}
	
	public Block getFirstBlock() {
		//��ȡ��������
		return null;
	}
	private void synchronousBlock(Block block) {
		//ͬ�����飬�����ڴ�ʹ��̵�һ��
		try {
			//�ļ�ĩ׷��һ��
			ArrayList<String> ls = new ArrayList<String>();
			ls.add(block.toJSONString());
			FileUtils.writeLines(new File(SMS.path+"blockchain"), "UTF-8",ls, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
