package sms.client;

import sms.MessageSender;

public class Login {
	//检查登录状态（后台线程运行），查询登录结果等。
	// !!只允许一个实例运行
	private static Login login;
	
	public static synchronized Login getInstance() {
		if(login==null) return new Login();
		return login;
	}
	
	/**
	 * 用于注册时的登录，账号是学校提供的。
	 * @param username 用户名
	 * @param password 密码
	 * @return 登录成功返回true，登录失败返回false
	 */
	public boolean login(String username, String password) {
		String message = "?action=login"+"&username="+username+"&password="+password;
		System.out.println("待发送消息："+message);
		MessageSender ms = MessageSender.getInstance();
		String r = ms.sendAndReceiveMessage("127.0.0.1", 1934, message);
		System.out.println("返回的消息为："+r);
		if(r.equals("success")) {
			System.out.println("登录成功");
			return true;
		}
		return false;
	}
}
