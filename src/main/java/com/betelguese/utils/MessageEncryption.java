package main.java.com.betelguese.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.java.com.betelguese.utils.helpers.Log;

public final class MessageEncryption {

	private static final String TAG = MessageEncryption.class.getSimpleName();

	public static final String encryptMessage(String message) {
		if (message == null) {
			return null;
		}
		String encryptedMessage = null;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(message.getBytes(), 0, message.length());
			encryptedMessage = new BigInteger(1,
					messageDigest.digest(messageDigest.digest(messageDigest
							.digest()))).toString(36);
			messageDigest.reset();
			Log.d(TAG, "message was encrypted successfully.");
			System.out.println("DATA  ENCRYTED!");
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, e.getClass().getSimpleName()
					+ " occured.Message encryption was failed.");
		}
		return encryptedMessage;
	}
}
