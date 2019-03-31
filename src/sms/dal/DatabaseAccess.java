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

/** 数据库访问
 * @author Chiuin
 *
 */
public class DatabaseAccess {
	private static volatile DatabaseAccess da;
	//不同的数据库有不同的驱动,com.mysql.jdbc.Driver已弃用
	private String driverName;   //请为mysql-connector-java-8.0.15.jar包配置classpath
    //sms为数据库名。？后面接参数，设置时区为GMT
	private String url;//jdbc:mysql://localhost:3306/sms?serverTimezone=GMT
	private String user;//root
	private String password;//123456
	private Connection conn = null;
	private boolean connect_success = false; //用于判断数据库是否连接成功的变量
	
	public static synchronized DatabaseAccess getInstance() {
		if(da==null) {
			synchronized (DatabaseAccess.class) {//双重加锁
                if (da == null) {
                	da = new DatabaseAccess();//不给da赋值，da就会永远null
                }
            }
		}
		return da;
	}
	
	/**
	 * 连接数据库
	 * @return 若连接成功，返回该DatabaseAccess自身；若连接失败，返回null
	 */
	public DatabaseAccess connect() {
		try {
			Class.forName(driverName);// 加载驱动。此步容易出错。请为mysql-connector-java-8.0.15.jar包配置classpath。
			conn = DriverManager.getConnection(url, user, password);
        	connect_success = true;
        	System.out.println("连接数据库成功。");
			return this;//da?this?
		} catch (ClassNotFoundException e) {
			System.out.println("连接数据库失败！ClassNotFoundException：加载驱动失败");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("连接数据库失败！SQL异常。");
			e.printStackTrace();
		}
		return null;
	}
	public DatabaseAccess connect(String url,String user,String password) {
		try {
			Class.forName(driverName);// 加载驱动
			conn = DriverManager.getConnection(url, user, password);
        	connect_success = true;
        	System.out.println("连接数据库成功。");
        	this.url = url;
        	this.user = user;
        	this.password = password;
			return da;
		} catch (ClassNotFoundException e) {
			System.out.println("连接数据库失败！ClassNotFoundException：加载驱动失败");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("连接数据库失败！SQL异常。");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 一般用于执行select语句。请先执行connect方法连接到数据库后再调用该方法。
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
			//todo：把dataset变成map列表
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
			return l;//todo:改
		}
		return null; //若没有返回，空集，返回null。
	}
	
	/**
	 * 一般用于执行INSERT, UPDATE or DELETE等不会返回ResultSet的语句。
	 * 请先执行connect方法连接到数据库后再调用该方法。
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
	 * 当你确定不再使用数据库时，用于关掉connection。
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("关闭数据库连接时发生错误！");
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
