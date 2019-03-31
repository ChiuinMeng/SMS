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
	 * �����¼����
	 * @return ����֤�ɹ�������true��
	 */
	public boolean login() {
		if(!map.containsKey("username")) return false; //��������username������ʧ�ܡ�
		String username = map.get("username");
		System.out.println("���ڼ���û���username="+username);
		String password_client = map.get("password");
		String sql = "select password from user_table where username="+username+";";
		List<Map<String, Object>> lm = null;
		try {
			lm = DatabaseAccess.getInstance().connect().executeQuery2(sql);
		} catch (SQLException e) {
			System.out.println("�����ݿ�ͨ��ʧ��");
			e.printStackTrace();
		}
		if(lm==null) { //ͨ��ʧ�ܣ����ؿռ������ᵼ��lmΪnull
			System.out.println("�����ڸ��û���");
			return false;
		}
		String password_server = (String) lm.get(0).get("password");
		if(password_client.equals(password_server)) {
			System.out.println("���ڸ��û�����������ȷ");
			return true;
		}
		return false;
	}
	
	/**
	 * �����ϴ���Կ������
	 * @return
	 */
	public boolean uploadPublicKey() {
		/*
		 * 1.��ȡmap�е��û���
		 * 2.��ȡmap�еĹ�Կ
		 */
		if(!map.containsKey("action") 
				|| !map.get("action").equals("upload") 
				|| !map.containsKey("username") 
				|| !map.containsKey("PublicKey")) {
			System.out.println("map�еĲ�������ȷ");
			return false;
		}
		String username = map.get("username");
		String publicKey = map.get("PublicKey");
		String sql = "UPDATE `sms`.`user_table` SET `public_key` = '"+publicKey+"' WHERE (`username` = '"+username+"');";
		try {
			DatabaseAccess.getInstance().connect().executeUpdate(sql );
			return true;
		} catch (SQLException e) {
			System.out.println("�ϴ���Կʧ��");
			e.printStackTrace();
		}
		return false;
	}
}
