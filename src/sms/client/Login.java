package sms.client;

import sms.MessageSender;

public class Login {
	//����¼״̬����̨�߳����У�����ѯ��¼����ȡ�
	// !!ֻ����һ��ʵ������
	private static Login login;
	
	public static synchronized Login getInstance() {
		if(login==null) return new Login();
		return login;
	}
	
	/**
	 * ����ע��ʱ�ĵ�¼���˺���ѧУ�ṩ�ġ�
	 * @param username �û���
	 * @param password ����
	 * @return ��¼�ɹ�����true����¼ʧ�ܷ���false
	 */
	public boolean login(String username, String password) {
		String message = "?action=login"+"&username="+username+"&password="+password;
		System.out.println("��������Ϣ��"+message);
		MessageSender ms = MessageSender.getInstance();
		String r = ms.sendAndReceiveMessage("127.0.0.1", 1934, message);
		System.out.println("���ص���ϢΪ��"+r);
		if(r.equals("success")) {
			System.out.println("��¼�ɹ�");
			return true;
		}
		return false;
	}
}
