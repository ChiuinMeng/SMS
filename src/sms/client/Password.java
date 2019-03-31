package sms.client;


import java.io.Console;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Password {
	
	/**
	 * 让用户输入密码，若能获取操作系统的控制台，密码不回显！！随后加密密码。
	 * @return 密码的SHA-256数字摘要
	 */
	public static String getEncryptedPassword() {
		Console console = System.console();
		if(console!=null) return encryptPassword(console.readPassword());
		return encryptPassword(new Scanner(System.in).nextLine().toCharArray());
	}
	
	/**
	 * 正在完善，不建议使用
	 * @param method
	 * @return
	 */
	public static String getEncryptedPassword(int method) {
		if(method==0) return getEncryptedPassword();
		else if(method==1) {
			return new Scanner(System.in).nextLine();
		}else if(method==2){
			return null;
		}else {
			return null;
		}
	}
	
	private static String encryptPassword(char[] cs) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			sha256.update(String.copyValueOf(cs).getBytes());
			byte[] b = sha256.digest();
			return new BigInteger(1,b).toString(16);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("字符串加密出错！");
			e.printStackTrace();
		}
		return null;
	}

}
