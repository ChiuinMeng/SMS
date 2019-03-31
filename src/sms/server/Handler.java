package sms.server;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import sms.dal.DatabaseAccess;

public class Handler {
	private Map<String, String> map;
	
	public Handler(Map<String, String> map) {
		this.map = map;
	}

	public boolean register() {
		return true;
	}
	
	/**
	 * 处理登录请求
	 * @return 若认证成功，返回true。
	 */
	public boolean login() {
		if(!map.containsKey("username")) return false; //若不包含username参数，失败。
		String username = map.get("username");
		System.out.println("正在检查用户：username="+username);
		String password_client = map.get("password");
		String sql = "select password from user_table where username="+username+";";
		List<Map<String, Object>> lm = null;
		try {
			lm = DatabaseAccess.getInstance().connect().executeQuery2(sql);
		} catch (SQLException e) {
			System.out.println("与数据库通信失败");
			e.printStackTrace();
		}
		if(lm==null) { //通信失败，返回空集，都会导致lm为null
			System.out.println("不存在该用户！");
			return false;
		}
		String password_server = (String) lm.get(0).get("password");
		if(password_client.equals(password_server)) {
			System.out.println("存在该用户，且密码正确");
			return true;
		}
		return false;
	}
	
	/**
	 * 处理上传公钥的请求
	 * @return
	 */
	public boolean uploadPublicKey() {
		/*
		 * 1.获取map中的用户名
		 * 2.获取map中的公钥
		 */
		if(!map.containsKey("action") 
				|| !map.get("action").equals("upload") 
				|| !map.containsKey("username") 
				|| !map.containsKey("PublicKey")) {
			System.out.println("map中的参数不正确");
			return false;
		}
		String username = map.get("username");
		String publicKey = map.get("PublicKey");
		String sql = "UPDATE `sms`.`user_table` SET `public_key` = '"+publicKey+"' WHERE (`username` = '"+username+"');";
		try {
			DatabaseAccess.getInstance().connect().executeUpdate(sql );
			return true;
		} catch (SQLException e) {
			System.out.println("上传公钥失败");
			e.printStackTrace();
		}
		return false;
	}
}
