package sms.dal;

/**
 * 区块的数据结�?
 * @author Chiuin
 *
 */
public class Block {
	int version; //版本�?
	String prevBlockHash;//父哈希，指向前一区块�?
	String merkleRoot;//Merkle根，梅克树，�?有交易的�?个汇总hash�?
	int timeStamp;//时间�?
	int difficutyTarget;
	int randomNumber;//随机数，也叫：Nonce
//	int next;//下一区块
	
	/**
version	4字节	版本号，⽤于跟踪软件/协议的更�?
prevBlockHash	32字节	上一个区块的Hash地址
merkleRoot	32字节	该区块中交易的merkl e树根的哈希�?�（稍后详细说明�?
time	4字节	该区块的创建时间�?
difficultyTarget	4字节	该区块链工作量证明难度目�?(稍后讲解工作量证�?)
nonce	4字节	用于证明工作量的计算参数
	 */
	//信息
	//大小
	//签名

	//持有者的公钥
	//记录数据
	//签名
}
