package sms;
import java.io.Serializable;
import java.security.interfaces.RSAPublicKey;

public class Config implements Serializable{
	private String serverIP;//��������ip��ַ
	private int port = 1934;//Ĭ�϶˿ں�
	private String schoolName;
	private RSAPublicKey schoolPublicKey;//ѧУ�Ĺ�Կ
	private RSAPublicKey localPublicKey;//�����Ĺ�Կ
	private int hostType;//�������ͣ����ڵ�1���νڵ�0��ѧУ������2
	private String databaseURL;//�磺"jdbc:mysql://localhost:3306/sms?serverTimezone=GMT"
	private String databaseUserName;
	private String databasePassword;
	private String databaseDriverName;
	
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public RSAPublicKey getSchoolPublicKey() {
		return schoolPublicKey;
	}
	public void setSchoolPublicKey(RSAPublicKey schoolPublicKey) {
		this.schoolPublicKey = schoolPublicKey;
	}
	public RSAPublicKey getLocalPublicKey() {
		return localPublicKey;
	}
	public void setLocalPublicKey(RSAPublicKey localPublicKey) {
		this.localPublicKey = localPublicKey;
	}
	public int getHostType() {
		return hostType;
	}
	public void setHostType(int hostType) {
		this.hostType = hostType;
	}
	public String getDatabaseURL() {
		return databaseURL;
	}
	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}
	public String getDatabaseUserName() {
		return databaseUserName;
	}
	public void setDatabaseUserName(String databaseUserName) {
		this.databaseUserName = databaseUserName;
	}
	public String getDatabasePassword() {
		return databasePassword;
	}
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
	public String getDatabaseDriverName() {
		return databaseDriverName;
	}
	public void setDatabaseDriverName(String databaseDriverName) {
		this.databaseDriverName = databaseDriverName;
	}
	
}