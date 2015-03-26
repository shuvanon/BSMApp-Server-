package main.java.com.betelguese.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.betelguese.database.DBQuery;
import main.java.com.betelguese.database.DatabaseService;
import main.java.com.betelguese.utils.MessageBuilder;
import main.java.com.betelguese.utils.RequestName;
import main.java.com.betelguese.utils.RequestName.SettingsRequest;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.ChangeName;
import main.java.com.betelguese.utils.items.ChangePassword;
import main.java.com.betelguese.utils.items.NewUser;
import main.java.com.betelguese.utils.items.SettingsItem;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;

public class SettingsService implements SettingsRequest, ServiceTag {
	/**
	 * TAG to identify the class from log.
	 */
	private static final String TAG = SettingsService.class.getSimpleName();
	/**
	 * TAG for the client receive service JSON.
	 */
	private static final String REQUEST_NAME = RequestName.SETTINGS_REQUEST;
	private ResultSet resultSet;
	private DatabaseService databaseService;

	public String getService(String serviceKey, String serviceValue) {
		Gson gson = new GsonBuilder().create();
		SettingsItem settingsItem = gson.fromJson(serviceValue,
				SettingsItem.class);
		databaseService = new DatabaseService();
		try {
			databaseService.open();
			if (settingsItem.getAdminId() == null) {
				return serverErrorMessage(new NullPointerException(
						"adminId was not valid."));
			} else {
				if (serviceKey.equals(CHANGE_NAME_PARAM)) {
					if (settingsItem.getChangeName() != null
							&& settingsItem.getChangePassword() == null
							&& settingsItem.getNewUser() == null) {
						return changeNameService(settingsItem.getChangeName(),
								settingsItem.getAdminId());
					} else {
						databaseService.close();
						return serverErrorMessage(new Exception(
								"Request was not permitted"));
					}
				} else if (serviceKey.equals(CHANGE_PASSWORD_PARAM)) {
					if (settingsItem.getChangeName() == null
							&& settingsItem.getChangePassword() != null
							&& settingsItem.getNewUser() == null) {
						return changePasswordService(settingsItem
								.getChangePassword());
					} else {
						databaseService.close();
						return serverErrorMessage(new Exception(
								"Request was not permitted"));
					}
				} else if (serviceKey.equals(CHANGE_NAME_PARAM)) {
					if (settingsItem.getChangeName() == null
							&& settingsItem.getChangePassword() == null
							&& settingsItem.getNewUser() != null) {
						return createUserService(settingsItem.getNewUser());
					} else {
						databaseService.close();
						return serverErrorMessage(new Exception(
								"Request was not permitted"));
					}
				} else {
					databaseService.close();
					return serverErrorMessage(new Exception(
							"Request was not permitted"));
				}
			}
		} catch (InstantiationException e) {
			return serverErrorMessage(e);
		} catch (IllegalAccessException e) {
			return serverErrorMessage(e);
		} catch (ClassNotFoundException e) {
			return serverErrorMessage(e);
		} catch (SQLException e) {
			return serverErrorMessage(e);
		}
	}

	private String changeNameService(ChangeName changeName, String adminId) {
		databaseService.execute(DBQuery.updateAdminName(
				changeName.getFirstName() + " " + changeName.getLastName(),
				adminId));
		ServiceMessage serviceMessage = new ServiceMessage(1,
				MessageBuilder.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

	private String changePasswordService(ChangePassword changePassword) {
		return null;
	}

	private String createUserService(NewUser newUser) {
		return null;
	}

	private String serverErrorMessage(Exception e) {
		Log.e(TAG, e.getClass().getSimpleName() + " occured" + e.getMessage(),
				e);
		ServiceMessage serviceMessage = new ServiceMessage(0,
				MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}
}
