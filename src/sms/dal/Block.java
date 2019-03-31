package sms.dal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 区块的数据结构
 * 一个区块存一个课程班的成绩单。
 * @author Chiuin
 *
 */
public class Block {
	private int version; //版本号
//	Block preBlock;
	private String prevBlockHash;//父哈希，指向前一区块
//	private String merkleRoot;//Merkle根，梅克树，所有记录的汇总hash值
	private long timeStamp;//时间戳
//	private int difficutyTarget;//困难指数，用于工作量证明
//	private int randomNumber;//随机数，也叫：Nonce
//	Block nextBlock;
//	String nextBlockHash
	private String creator;//区块创建者
	private String signature;//区块创建者签名
	
	private String info;
	
	
	public Block(int version,String prevBlockHash,long timeStamp){
		this.version = version;
		this.prevBlockHash = prevBlockHash;
		this.timeStamp = timeStamp;
	}
	
	/**
	 * 调用此方法会生成一个符合规范的区块，
	 * 通过new生成的Block只包含一些初始信息，并不符合规范。
	 * @return
	 */
	public Block generateBlock() {
		//todo:
		
		if(checkBlock(this)) return this;
		return null;
	}
	
	/**
	 * 调用此方法可检查传入的区块是否符合规范
	 * @param block
	 * @return
	 */
	public static boolean checkBlock(Block block) {
		//todo:什么样的区块是符合规范的？
		return false;
	}
}
