package main.java.com.betelguese.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.java.com.betelguese.utils.helpers.Log;

public class DatabaseService implements DBUtils {

	private DBUser dbUser;
	private Statement statement;
	private Connection connection;
	private static final String TAG = DatabaseService.class.getSimpleName();

	public DatabaseService() {
		dbUser = new DBUser();
	}

	public DatabaseService(String username, String password, String driverName,
			String databaseUrl) {
		dbUser = new DBUser(username, password, driverName, databaseUrl);
	}

	public void open() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		if (dbUser != null) {
			Class.forName(dbUser.getDriverName()).newInstance();
			connection = getConnection();
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			statement = (Statement) connection.createStatement();
			return statement != null ? statement.executeQuery(query) : null;
		} catch (NullPointerException e) {
			Log.e(TAG,
					e.getClass().getSimpleName()
							+ " occured.Unable to execute query and return a resultset."
							+ e.getMessage(), e);
			return null;
		} catch (SQLException e) {
			Log.e(TAG,
					e.getClass().getSimpleName()
							+ " occured.Unable to execute query and return a resultset."
							+ e.getMessage(), e);
			return null;
		}

	}

	public boolean execute(String query) {
		try {
			statement = (Statement) connection.createStatement();
			return statement != null ? statement.execute(query) : false;
		} catch (NullPointerException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.Unable to execute query." + e.getMessage(), e);
			return false;
		} catch (SQLException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.Unable to execute query." + e.getMessage(), e);
			return false;
		}

	}

	public void close() {

	}

	private Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName(dbUser.getDriverName()).newInstance();
		return DriverManager.getConnection(dbUser.getDatabaseUrl(),
				dbUser.getUsername(), dbUser.getPassword());
	}

}
