package sms.dal;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateCrtKey;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import sms.Security;

/**
 * 区块的数据结构
 * 一个区块存一个课程班的成绩单。
 * @author Chiuin
 *
 */
public class Block implements Serializable{
	private static final long serialVersionUID = -1837262036470313162L;
	private int version; //版本号
	private String prevBlockHash;//父哈希，指向前一区块
	private long timeStamp;//时间戳
	private String creatorPublicKey;//本区块创建者的公钥
	private String signature;//本区块创建者的数字签名	
	private String info;
//	Block preBlock;
//	private String merkleRoot;//Merkle根，梅克树，所有记录的汇总hash值
//	private int difficutyTarget;//困难指数，用于工作量证明
//	private int randomNumber;//随机数，也叫：Nonce
//	Block nextBlock;
//	String nextBlockHash
	
	public Block(int version,String prevBlockHash,long timeStamp,String creatorPublicKey,String info){
		this.version = version;
		this.prevBlockHash = prevBlockHash;
		this.timeStamp = timeStamp;
		this.creatorPublicKey = creatorPublicKey;
		this.info = info;
//		System.out.println(info);
	}
	/**
	 * 调用此方法会生成一个符合规范的区块，
	 * 通过new生成的Block只包含一些初始信息，并不符合规范。
	 * @return
	 */
	public Block generateBlock(RSAPrivateCrtKey privateKey) {
		//获取哈希值
		String hash = getDigitalMessage(this);
		this.signature =  Security.encryptString(hash,privateKey);//使用自己的私钥签名
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
		String hash2 = Security.decryptString(block.getSignature(), Security.getPublicKeyFromString(block.creatorPublicKey));
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
		str += block.getCreatorPublicKey();
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
	public String getCreatorPublicKey() {
		return creatorPublicKey;
	}
	public String getSignature() {
		return signature;
	}
	public String getInfo() {
		return info;
	}
	public String toJSONString() {
//		Object o = JSONObject.toJSON(this);
//		return JSONObject.toJSONString(o,true);
//		String str = JSONObject.toJSONString(this);//返回的info带有\
//		return str.replaceAll("\\\\", "");
		return JSONObject.toJSONString(this);
	}
}
