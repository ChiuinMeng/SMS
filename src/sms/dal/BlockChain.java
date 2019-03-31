package sms.dal;

import java.util.ArrayList;

/**
 * 区块链的数据结构
 * @author Chiuin
 *
 */
public class BlockChain {
	ArrayList<Block> blockchain = new ArrayList<Block>();

	/**
	 * 添加新的区块
	 * @param firstBlock
	 */
	public void add(Block block) {
		//判断是否有权限add
		blockchain.add(block);
	}
	
	/**
	 * 创建创世区块
	 * @param firstBlock
	 */
	public void createFirstBlock(Block firstBlock) {
		//判断是否有权限add
		blockchain.add(firstBlock);
	}
	
	/**
	 * 获取创世区块信息
	 * @return
	 */
	public Block getFirstBlock() {
		return blockchain.get(0);
	}
}
