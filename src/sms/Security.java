package sms;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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
 * �밲ȫ�йص��࣬�ṩ��Կ���ɣ����ܽ��ܵȡ�
 * @author Chiuin
 *
 */
public class Security {
	private RSAPublicKey publicKey;
	private RSAPrivateCrtKey privateKey;
	public static final int KeyType_Public = 1;
	public static final int KeyType_Private = 2;
	
	/**
	 * ���ɹ�˽Կ�׶ԣ��Ժ����getPublicKey()��getPrivateKey()���
	 * @return ���ɹ�������Կ�ԣ�����true��
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
	 * ���ȵ���generateKeyPair()���ٵ��ø÷�����
	 * @return ���ؾ�base64�������ַ�������Կ
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
	 * ���ȵ���generateKeyPair()���ٵ��ø÷���
	 * @return ���ؾ�base64�������ַ�����˽Կ
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
	 * ʹ��RSA��Կ����˽Կ�������ַ�����
	 * RSA���ܺ���ֽ����龭��Base64���룬��ת��Ϊ�ַ�����
	 * @param str �������ַ��� 
	 * @param key RSA��Կ��˽Կ
	 * @return ���ܺ���ַ����������й�Base64���롣���������󣬷���null��
	 */
	public static String encryptString(String str,Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] ciphertext = cipher.doFinal(str.getBytes());//�Ѽ����ֽ�����
			byte[] ciphertext_base64 = Base64.getEncoder().encode(ciphertext);//��base64������Ѽ����ֽ�����
			String ciphertext_str = new String(ciphertext_base64);//��base64������Ѽ����ַ���
			return ciphertext_str ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * �����ַ��������ַ�����þ���RSA���ܺ���й�Base64���롣
	 * @param str �����ܵ��ַ�����
	 * @param key ����Կ��
	 * @return ���ܺ�����ġ����������󣬷���null��
	 */
	public static String decryptString(String str,Key key) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] ciphertext_user2 = Base64.getDecoder().decode(str);//�Ƚ�������
			byte[] plaintext = cipher.doFinal(ciphertext_user2);//��RSA��������
			String plaintext_str = new String(plaintext);
			return plaintext_str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ָ���Ĺ�Կ�ļ�������RSAPublicKey����
	 * ���ļ�����ǹ�Կ��base64�������ַ���
	 * @param pathname ��Կ�ļ���ַ,���ļ�·�������֡�������Ϣ��
	 * @return RSAPublicKey����
	 * @throws NoSuchAlgorithmException KeyFactory.getInstance("RSA")����
	 * @throws InvalidKeySpecException keyFactory.generatePublic(pubKeySpec)����
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
	 * ��ָ����˽Կ�ļ�������RSAPrivateCrtKey����
	 * ���ļ������˽Կ��base64�������ַ���
	 * @param pathname ˽Կ�ļ���ַ,���ļ�·�������֡�������Ϣ��
	 * @return RSAPrivateCrtKey����
	 * @throws NoSuchAlgorithmException KeyFactory.getInstance("RSA")����
	 * @throws InvalidKeySpecException (RSAPrivateCrtKey)keyFactory1.generatePrivate(priKeySpec)����
	 */
	public static RSAPrivateCrtKey getPrivateKeyFromFile(String pathname) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String key_str = FileTool.readFile(pathname);
		byte[] key_byte = Base64.getDecoder().decode(key_str.getBytes());
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(key_byte);
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey)keyFactory1.generatePrivate(priKeySpec);
        return priKey;
	}
	
	/**
	 * ��Կ����base64���룬�����ַ���
	 * @param key
	 * @return
	 */
	public static String keyToString_Base64(Key key) {
		return new String(Base64.getEncoder().encode(key.getEncoded()));
	}
	
	/**
	 *  ��ȡĳ���ַ���������ժҪ��
	 *  method����ΪSHA-256��
	 * @param content 
	 * @param method SHA-256,MD5��
	 * @return
	 */
	public static String getDigitalMessage(String content,String method) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(method);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("���뷽������");
			e.printStackTrace();
		}
		if(md!=null) {
			md.update(content.getBytes());
			byte[] d = md.digest();
			return new BigInteger(1,d).toString(16);
		}
		return null;
	}

	public static String encryptString(String content, String keyFilePath,int type) {
		try {
			if(type==KeyType_Private) {
				return encryptString(content, getPrivateKeyFromFile(keyFilePath));
			}else if(type==KeyType_Public) {
				return encryptString(content, getPublicKeyFromFile(keyFilePath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RSAPublicKey getPublicKeyFromString(String publicKey) {
		try {
			byte[] key_byte = Base64.getDecoder().decode(publicKey.getBytes());
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key_byte);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
			return pubKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static RSAPrivateCrtKey getPrivateKeyFromString(String privateKey) {
		try {
			byte[] key_byte = Base64.getDecoder().decode(privateKey.getBytes());
	        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(key_byte);
	        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
	        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey)keyFactory1.generatePrivate(priKeySpec);
	        return priKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
