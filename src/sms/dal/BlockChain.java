package sms.dal;

import java.util.ArrayList;

/**
 * �����������ݽṹ
 * @author Chiuin
 *
 */
public class BlockChain {
	ArrayList<Block> blockchain = new ArrayList<Block>();

	/**
	 * ����µ�����
	 * @param firstBlock
	 */
	public void add(Block block) {
		//�ж��Ƿ���Ȩ��add
		blockchain.add(block);
	}
	
	/**
	 * ������������
	 * @param firstBlock
	 */
	public void createFirstBlock(Block firstBlock) {
		//�ж��Ƿ���Ȩ��add
		blockchain.add(firstBlock);
	}
	
	/**
	 * ��ȡ����������Ϣ
	 * @return
	 */
	public Block getFirstBlock() {
		return blockchain.get(0);
	}
}
