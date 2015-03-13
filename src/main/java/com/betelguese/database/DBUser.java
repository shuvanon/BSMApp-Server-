package main.java.com.betelguese.database;

import main.java.com.betelguese.utils.helpers.Log;

public class DBUser {

	private static final String TAG = DBUser.class.getSimpleName();
	private String username = "adminJzJxgDI";
	private String password = "BHLvW_J8CJpk";
	private String driverName = "com.mysql.jdbc.Driver";
	private String databaseUrl = "jdbc:mysql://" + OPENSHIFT_MYSQL_DB_HOST
			+ ":" + OPENSHIFT_MYSQL_DB_PORT
			+ "/bsmapp?useUnicode=true&characterEncoding=UTF-8";
	public static final String OPENSHIFT_MYSQL_DB_HOST = System
			.getenv("OPENSHIFT_MYSQL_DB_HOST");
	public static final String OPENSHIFT_MYSQL_DB_PORT = System
			.getenv("OPENSHIFT_MYSQL_DB_PORT");

	public DBUser() {
	}

	public DBUser(String username, String password, String driverName,
			String databaseUrl) {
		this.username = username;
		this.password = password;
		this.driverName = driverName;
		this.databaseUrl = databaseUrl;
		Log.d(TAG, "new database username and password set.");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
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

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
}
