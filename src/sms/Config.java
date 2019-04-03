package sms;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Config implements Serializable{
	private static final long serialVersionUID = 8089987927733836847L;
	private String hostType;//主机类型，主节点1，次节点0，学校服务器2
	private String operateSystem;
	private String schoolName;
	private String serverHost;//学校服务器主机，如：www.stu.edu.cn、127.0.0.1
	private int port = 1934;//默认端口号
	private Key key;
	private Database database;
	private boolean hadRunOnce;
	
	
	public Config(JSONObject config) {
		hostType = config.getString("hostType");
		operateSystem = System.getProperty("os.name").toLowerCase();
		schoolName = config.getString("schoolName");
		serverHost = config.getString("serverHost");
		port = config.getIntValue("port");
		key = new Key((JSONObject)config.get("key"));
		database = new Database((JSONObject)config.get("database"));
		hadRunOnce = config.getBooleanValue("hadRunOnce");
	}

	public String toJSONStringFormat() {
		JSONObject json = (JSONObject)JSONObject.toJSON(this);
		return JSON.toJSONString(json,SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
	}
	public String toJSONString() {
		JSONObject json = (JSONObject)JSONObject.toJSON(this);
		return json.toJSONString();
	}
	public int getPort() {
		return port;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public String getHostType() {
		return hostType;
	}
	public String getOperateSystem() {
		return operateSystem;
	}
	public String getServerHost() {
		return serverHost;
	}
	public Key getKey() {
		return key;
	}
	public Database getDatabase() {
		return database;
	}
	public boolean isHadRunOnce() {
		return hadRunOnce;
	}
	public void setHadRunOnce(boolean hadRunOnce) {
		this.hadRunOnce = hadRunOnce;
	}




	public class Key{
		private String schoolPublicKey;//学校的公钥
		private String localPublicKey;//本机的公钥
		Key(JSONObject key){
			if(key==null) return;
			schoolPublicKey =key.getString("schoolPublicKey");
			localPublicKey = key.getString("localPublicKey");
		}
		public String getSchoolPublicKey() {
			return schoolPublicKey;
		}
		public String getLocalPublicKey() {
			return localPublicKey;
		}
		public void setSchoolPublicKey(String schoolPublicKey) {
			this.schoolPublicKey = schoolPublicKey;
		}
		public void setLocalPublicKey(String localPublicKey) {
			this.localPublicKey = localPublicKey;
		}
	}
	class Database{
		private String url;//如："jdbc:mysql://localhost:3306/sms?serverTimezone=GMT"
		private String userName;
		private String password;
		private String driverName;	
		Database(JSONObject database){
			if(database==null) return;
			url = database.getString("url");
			userName = database.getString("userName");
			password = database.getString("password");
			driverName = database.getString("driverName");
		}
		public String getUrl() {
			return url;
		}
		public String getUserName() {
			return userName;
		}
		public String getPassword() {
			return password;
		}
		public String getDriverName() {
			return driverName;
		}
	}
}