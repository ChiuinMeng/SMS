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
		 * 1. 验证区块，判断是否有权限add
		 * 2. 内存添加区块
		 * 3. 磁盘同步区块
		 * 4. 广播
		 */
		//1.区块合乎规范&&创建者为学校
		if(Block.checkBlock(block) && block.getCreatorPublicKey().equals(schoolPublicKey)) {
			//2. 内存添加区块
			chain.add(block);
			//3. 磁盘同步区块
			synchronousBlock(block);
			//4. 创建区块后，要广播该区块
			PeerManagement.broadcastUDPMessage(block.getInfo(),SMS.config.getPort());
		}
	}
	
	public Block getFirstBlock() {
		//获取创世区块
		return null;
	}
	private void synchronousBlock(Block block) {
		//同步区块，保持内存和磁盘的一致
		try {
			//文件末追加一行
			ArrayList<String> ls = new ArrayList<String>();
			ls.add(block.toJSONString());
			FileUtils.writeLines(new File(SMS.path+"blockchain"), "UTF-8",ls, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
