package main.java.com.betelguese.services;

import main.java.com.betelguese.utils.RequestName.SettingsRequest;

public class SettingsService implements SettingsRequest, ServiceTag {

	public String getService(String serviceKey, String serviceValue) {
		if (serviceKey.equals(CHANGE_NAME_PARAM)) {

		}
		return null;
	}

	private String changeNameService(String serviceValue) {
		return null;
	}
}
