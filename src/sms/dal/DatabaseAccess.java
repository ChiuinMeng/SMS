/**
 * 
 */
package sms.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/** ���ݿ����
 * @author Chiuin
 *
 */
public class DatabaseAccess {
	private static volatile DatabaseAccess da;
	//��ͬ�����ݿ��в�ͬ������,com.mysql.jdbc.Driver������
	private String driverName;   //��Ϊmysql-connector-java-8.0.15.jar������classpath
    //smsΪ���ݿ�����������Ӳ���������ʱ��ΪGMT
	private String url;//jdbc:mysql://localhost:3306/sms?serverTimezone=GMT
	private String user;//root
	private String password;//123456
	private Connection conn = null;
	private boolean connect_success = false; //�����ж����ݿ��Ƿ����ӳɹ��ı���
	
	public static synchronized DatabaseAccess getInstance() {
		if(da==null) {
			synchronized (DatabaseAccess.class) {//˫�ؼ���
                if (da == null) {
                	da = new DatabaseAccess();//����da��ֵ��da�ͻ���Զnull
                }
            }
		}
		return da;
	}
	
	/**
	 * �������ݿ�
	 * @return �����ӳɹ������ظ�DatabaseAccess����������ʧ�ܣ�����null
	 */
	public DatabaseAccess connect() {
		try {
			Class.forName(driverName);// �����������˲����׳�����Ϊmysql-connector-java-8.0.15.jar������classpath��
			conn = DriverManager.getConnection(url, user, password);
        	connect_success = true;
        	System.out.println("�������ݿ�ɹ���");
			return this;//da?this?
		} catch (ClassNotFoundException e) {
			System.out.println("�������ݿ�ʧ�ܣ�ClassNotFoundException����������ʧ��");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("�������ݿ�ʧ�ܣ�SQL�쳣��");
			e.printStackTrace();
		}
		return null;
	}
	public DatabaseAccess connect(String url,String user,String password) {
		try {
			Class.forName(driverName);// ��������
			conn = DriverManager.getConnection(url, user, password);
        	connect_success = true;
        	System.out.println("�������ݿ�ɹ���");
        	this.url = url;
        	this.user = user;
        	this.password = password;
			return da;
		} catch (ClassNotFoundException e) {
			System.out.println("�������ݿ�ʧ�ܣ�ClassNotFoundException����������ʧ��");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("�������ݿ�ʧ�ܣ�SQL�쳣��");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * һ������ִ��select��䡣����ִ��connect�������ӵ����ݿ���ٵ��ø÷�����
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		if(connect_success) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ps.close();
			return rs;
		}
		return null;
	}
	public List<Map<String,Object>> executeQuery2(String sql) throws SQLException {
		if(connect_success) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs==null) {
				ps.close();
				return null;
			}
			//todo����dataset���map�б�
			List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
			ResultSetMetaData rsm = rs.getMetaData();
			int n = rsm.getColumnCount();
			while(rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				for(int i=1;i<=n;i++) {
					String cn = rsm.getColumnName(i);
					map.put(cn, rs.getObject(i));
				}
				l.add(map);
			}
			ps.close();
			return l;//todo:��
		}
		return null; //��û�з��أ��ռ�������null��
	}
	
	/**
	 * һ������ִ��INSERT, UPDATE or DELETE�Ȳ��᷵��ResultSet����䡣
	 * ����ִ��connect�������ӵ����ݿ���ٵ��ø÷�����
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		if(connect_success) {
			PreparedStatement ps = conn.prepareStatement(sql);
			int r = ps.executeUpdate();
			ps.close();
			return r;
		}
		return -2;
	}
	
	/**
	 * ����ȷ������ʹ�����ݿ�ʱ�����ڹص�connection��
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("�ر����ݿ�����ʱ��������");
			e.printStackTrace();
		}
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
}
