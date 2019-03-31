package sms.dal;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import sms.Security;

/**
 * 区块的数据结构
 * 一个区块存一个课程班的成绩单。
 * @author Chiuin
 *
 */
public class Block {
	private int version; //版本号
	private String prevBlockHash;//父哈希，指向前一区块
	private long timeStamp;//时间戳
	private RSAPublicKey creatorPublicKey;//本区块创建者的公钥
	private String signature;//本区块创建者的数字签名	
	private String info;
//	Block preBlock;
//	private String merkleRoot;//Merkle根，梅克树，所有记录的汇总hash值
//	private int difficutyTarget;//困难指数，用于工作量证明
//	private int randomNumber;//随机数，也叫：Nonce
//	Block nextBlock;
//	String nextBlockHash
	
	public Block(int version,String prevBlockHash,long timeStamp,RSAPublicKey creatorPublicKey,String info){
		this.version = version;
		this.prevBlockHash = prevBlockHash;
		this.timeStamp = timeStamp;
		this.creatorPublicKey = creatorPublicKey;
		this.info = info;
	}
	/**
	 * 调用此方法会生成一个符合规范的区块，
	 * 通过new生成的Block只包含一些初始信息，并不符合规范。
	 * @return
	 */
	public Block generateBlock(RSAPrivateCrtKey privateKey) {
		//获取哈希值
		String hash = getDigitalMessage(this);
		this.signature =  Security.encryptString(hash,privateKey);
		if(checkBlock(this)) return this;
		return null;
	}
	/**
	 * 调用此方法可检查传入的区块是否符合规范
	 * @param block
	 * @return
	 */
	public static boolean checkBlock(Block block) {
		String hash1 = getDigitalMessage(block);
		String hash2 = Security.decryptString(block.getSignature(), block.creatorPublicKey);
		if(hash1.equals(hash2)) return true;
		return false;
	}

	/**
	 * 获取某区块的数字摘要
	 * @return
	 */
	public static String getDigitalMessage(Block block) {
		String str = String.valueOf(block.getVersion());
		str += block.getPrevBlockHash();
		str += String.valueOf(block.getTimeStamp());
		str += Security.keyToString_Base64(block.getCreatorPublicKey());
		str += block.getInfo();
		return Security.getDigitalMessage(str,"SHA-256");
	}
	
	public int getVersion() {
		return version;
	}
	public String getPrevBlockHash() {
		return prevBlockHash;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public RSAPublicKey getCreatorPublicKey() {
		return creatorPublicKey;
	}
	public String getSignature() {
		return signature;
	}
	public String getInfo() {
		return info;
	}
}
