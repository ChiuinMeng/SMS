package sms.client;

public class Test {

	public static void main(String[] args) {
//		String path = System.getProperty("user.home")+"\\.sms\\";
//		System.out.println(path);
//		path = path.replaceAll("\\\\","/");
//		System.out.println(path);
		
//		long t = System.currentTimeMillis();
//		System.out.println(t);
//		System.out.println(String.valueOf(t));
		
		new LoginProcedure("127.0.0.1",1934).login();
	}
}
