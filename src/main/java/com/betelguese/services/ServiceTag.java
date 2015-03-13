package main.java.com.betelguese.services;

public interface ServiceTag {
	public static final int SUCCESS_SERVICE_BUT_NO_DATA = -5;
	public static final int LOG_IN_ERROR = -4;
	public static final int LESS_PARAM_SERVICE = -3;
	public static final int UNAUTHORIZED_SERVICE = -1;
	public static final int EMPTY_CLIENT_SERVICE = -2;
	public static final int SERVER_ERROR = 0;
	public static final int SUCCESS_SERVICE = 1;

}
