package sms;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import com.mysql.cj.util.Base64Decoder;

/**
 * 与安全有关的类，提供密钥生成，加密解密等。
 * @author Chiuin
 *
 */
public class Security {
	private RSAPublicKey publicKey;
	private RSAPrivateCrtKey privateKey;
	
	/**
	 * 生成公私钥匙对，稍后调用getPublicKey()和getPrivateKey()获得
	 * @return 若成功生成密钥对，返回true。
	 */
	public boolean generateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.generateKeyPair();	
			publicKey = (RSAPublicKey) kp.getPublic();
			privateKey = (RSAPrivateCrtKey) kp.getPrivate();
			return true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 请先调用generateKeyPair()，再调用该方法。
	 * @return 返回经base64编码后的字符串，公钥
	 */
	public String getPublicKey() {
		return new String(getPublicKey_base64());
	}
	public byte[] getPublicKey_plain() {
		return publicKey.getEncoded();
	}
	public byte[] getPublicKey_base64() {
		return Base64.getEncoder().encode(publicKey.getEncoded());
	}
	public RSAPublicKey getRSAPublicKey() {
		return publicKey;
	}
	/**
	 * 请先调用generateKeyPair()，再调用该方法
	 * @return 返回经base64编码后的字符串，私钥
	 */
	public String getPrivateKey() {
		return new String(getPrivateKey_base64());
	}
	public byte[] getPrivateKey_plain() {
		return privateKey.getEncoded();
	}
	public byte[] getPrivateKey_base64() {
		return Base64.getEncoder().encode(privateKey.getEncoded());
	}
	public RSAPrivateCrtKey getRSAPrivateCrtKey() {
		return privateKey;
	}
	
	/**
	 * 使用RSA公钥或者私钥来加密字符串。
	 * RSA加密后的字节数组经过Base64编码，才转化为字符串。
	 * @param str 待加密字符串 
	 * @param key RSA公钥或私钥
	 * @return 加密后的字符串，并进行过Base64编码。若发生错误，返回null。
	 */
	public String encryptString(String str,Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] ciphertext = cipher.doFinal(str.getBytes());//已加密字节数组
			byte[] ciphertext_base64 = Base64.getEncoder().encode(ciphertext);//经base64编码的已加密字节数组
			String ciphertext_str = new String(ciphertext_base64);//经base64编码的已加密字符串
			return ciphertext_str ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * 解密字符串，该字符串须得经过RSA加密后进行过Base64编码。
	 * @param str 待解密得字符串。
	 * @param key 解密钥匙
	 * @return 解密后的明文。若发生错误，返回null。
	 */
	public String decryptString(String str,Key key) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] ciphertext_user2 = Base64.getDecoder().decode(str);//先解码密文
			byte[] plaintext = cipher.doFinal(ciphertext_user2);//再RSA解密密文
			String plaintext_str = new String(plaintext);
			return plaintext_str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将指定的公钥文件，生成RSAPublicKey对象。
	 * 该文件存的是公钥经base64编码后的字符串
	 * @param pathname 公钥文件地址,含文件路径、名字、类型信息。
	 * @return RSAPublicKey对象
	 * @throws NoSuchAlgorithmException KeyFactory.getInstance("RSA")错误
	 * @throws InvalidKeySpecException keyFactory.generatePublic(pubKeySpec)错误
	 */
	public static RSAPublicKey getPublicKeyFromFile(String pathname) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String key_str = FileTool.readFile(pathname);
		byte[] key_byte = Base64.getDecoder().decode(key_str.getBytes());
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key_byte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
		return pubKey;
	}
	/**
	 * 将指定的私钥文件，生成RSAPrivateCrtKey对象。
	 * 该文件存的是私钥经base64编码后的字符串
	 * @param pathname 私钥文件地址,含文件路径、名字、类型信息。
	 * @return RSAPrivateCrtKey对象
	 * @throws NoSuchAlgorithmException KeyFactory.getInstance("RSA")错误
	 * @throws InvalidKeySpecException (RSAPrivateCrtKey)keyFactory1.generatePrivate(priKeySpec)错误
	 */
	public static RSAPrivateCrtKey getPrivateKeyFromFile(String pathname) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String key_str = FileTool.readFile(pathname);
		byte[] key_byte = Base64.getDecoder().decode(key_str.getBytes());
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(key_byte);
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey)keyFactory1.generatePrivate(priKeySpec);
        return priKey;
	}
}
