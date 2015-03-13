package main.java.com.betelguese.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.betelguese.database.DBQuery;
import main.java.com.betelguese.database.DatabaseService;
import main.java.com.betelguese.utils.MessageBuilder;
import main.java.com.betelguese.utils.MessageEncryption;
import main.java.com.betelguese.utils.RequestName;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.AdminInfo;
import main.java.com.betelguese.utils.json.builders.LogInMessage;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;

/**
 * {@link LogInService} class is for client app to log in to the main activity
 * of the application. Client ask for log in to the application by their own
 * userName & password. Server will check the user name and password in the
 * database whether the user is register or not. Is the user is registered and
 * the password is wrong and vice versa.Server will decline the request.
 * 
 * To use this class follow the code snippet below to create an object of it.
 * <code>
 * LogInService logInService = new LogInService();
 * </code>
 * 
 * 
 * * If and only if the the requestName is <code>logInRequest</code> along with
 * the user name & password then simply call it method
 * {@link LogInService#logInValidity(String, String)} after creating the object
 * of {@link LogInService}.The code snippet to use this method is given below.
 * 
 * 
 * <code>
 * logInService.logInValidity(userName,password);
 * </code>
 * 
 * @author tuman
 * @version 1.0
 */
public class LogInService implements ServiceTag {

	/**
	 * TAG to identify the class from log.
	 */
	private static final String TAG = LogInService.class.getSimpleName();
	/**
	 * TAG for the client receive service JSON.
	 */
	private static final String REQUEST_NAME = RequestName.LOG_IN_REQUEST;
	private ResultSet resultSet;

	public LogInService() {
	}

	/**
	 * Client calls the <code>logInRequest</code> request with a
	 * <code>userName</code> and a <code>password</code> parameter.If the
	 * <code>logInRequest</code> is requested with both of those parameter, The
	 * server calls {@link #logInValidity(String, String)} to check the
	 * validation of the client log in request.
	 * 
	 * @param userName
	 * @param password
	 * @return the JSON with successful on unsuccessful message
	 */
	public String logInValidity(final String userName, final String password) {
		DatabaseService databaseService = new DatabaseService();
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery.getUserProfile(
					userName, MessageEncryption.encryptMessage(password)));
			databaseService.close();
			if (resultSet.next()) {
				return logInSuccess(resultSet);
			} else {
				return UnknownUser();
			}
		} catch (InstantiationException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.log in validation was not successful.", e);
			return logInFailed();
		} catch (IllegalAccessException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.log in validation was not successful.", e);
			return logInFailed();
		} catch (ClassNotFoundException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.log in validation was not successful.", e);
			return logInFailed();
		} catch (SQLException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.log in validation was not successful.", e);
			return logInFailed();
		}

	}

	/**
	 * 
	 * @param resultSet
	 * @return successful log in JSON
	 * @throws SQLException
	 */
	private String logInSuccess(final ResultSet resultSet) throws SQLException {
		LogInMessage logInMessage = new LogInMessage(createProfile(resultSet));
		logInMessage.setDone(1);
		logInMessage.setMessage(MessageBuilder.messageBuilder(SUCCESS_SERVICE,
				REQUEST_NAME));
		logInMessage.setRequestName(REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(logInMessage);

	}

	/**
	 * 
	 * @return unsuccessful log in info JSON
	 */
	private String logInFailed() {
		ServiceMessage serviceMessage = new ServiceMessage(0,
				MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

	/**
	 * 
	 * @return unsuccessful log in info for user not registered in JSON
	 */
	private String UnknownUser() {
		ServiceMessage serviceMessage = new ServiceMessage(0,
				MessageBuilder.messageBuilder(LOG_IN_ERROR), REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

	/**
	 * 
	 * @param resultSet
	 * 
	 * @return The profile of the admin who wanted to log in;
	 * @throws SQLException
	 */
	private AdminInfo createProfile(ResultSet resultSet) throws SQLException {
		AdminInfo profile = new AdminInfo();
		profile.setAdminId(resultSet.getString("administrator_id"));
		profile.setUserName(resultSet.getString("user_name"));
		profile.setAdminName(resultSet.getString("administrator_name"));
		profile.setAdministratorLevel(resultSet
				.getString("administrator_level"));
		return profile;
	}
}
