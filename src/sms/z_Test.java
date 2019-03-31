package sms;
import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class z_Test {
	
	public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		System.out.println(Security.getDigitalMessage("name", "SHA-256"));
		System.out.println(Security.getDigitalMessage("name", "md5"));
//		Security s = new Security();
//		s.generateKeyPair();
//		
//		System.out.println(s.getPublicKey());
//		System.out.println(s.getPrivateKey());
//		
//		FileTool.crateFile("C:\\Users\\Chiuin\\Downloads\\pub", s.getPublicKey());
//		FileTool.crateFile("C:\\Users\\Chiuin\\Downloads\\pri", s.getPrivateKey());
//		
//		byte [] pub = Security.getPublicKeyFromFile("C:\\Users\\Chiuin\\Downloads\\pub").getEncoded();
//		System.out.println();
//		System.out.println(new String(Base64.getEncoder().encode(pub)));
//		byte[] pri = Security.getPrivateKeyFromFile("C:\\Users\\Chiuin\\Downloads\\pri").getEncoded();
//		System.out.println(new String(Base64.getEncoder().encode(pri)));
	}
}
