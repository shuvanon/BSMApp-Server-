package main.java.com.betelguese.utils.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

	public Log() {

	}

	public static void d(final String TAG, final String debugMessage) {
		System.out.println(TAG + ":\t" + debugMessage);
		Logger.getLogger(TAG).log(Level.INFO, debugMessage);
	}

	public static void e(final String TAG, final String errorMessage,
			final Exception error) {
		System.err.println(TAG + ":\t\t" + errorMessage);
		Logger.getLogger(TAG).log(Level.SEVERE, errorMessage, error);
	}

}
