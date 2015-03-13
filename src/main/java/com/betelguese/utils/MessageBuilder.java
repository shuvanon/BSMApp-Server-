package main.java.com.betelguese.utils;

public final class MessageBuilder {

	private static final int SUCCESS_SERVICE_BUT_NO_DATA = -5;
	private static final int LOGIN_ERROR_TAG = -4;
	private static final int LESS_PARAM_TAG = -3;
	private static final int EMPTY_CLIENT_TAG = -2;
	private static final int UNAUTHORIZED_TAG = -1;
	private static final int SERVER_ERROR_TAG = 0;
	private static final int SUCCESS_TAG = 1;

	private static final String LOGIN_MESSAGE = " Username or password did'nt match or the user is not registered.";
	private static final String LESS_PARAM_MESSAGE = " was requested with less parameter.";
	private static final String EMPTY_CLIENT_MESSAGE = "Server didn't receive any request.";
	private static final String UNAUTHORIZED_MESSAGE = " is not authorized by the server.";
	private static final String SERVER_ERROR_MESSAGE = " can't be processed by the server at this moment.";
	private static final String SUCCESS_MESSAGE = " was processed by the server successfully.";
	private static final String SUCCESS_SERVICE_BUT_NO_DATA_MESSAGE = " was processed by the server successfully.But no data was found for the service.";

	private static StringBuilder stringBuilder;

	public MessageBuilder() {
	}

	public static final String messageBuilder(final int service) {
		return messageBuilder(service, null);
	}

	public static final String messageBuilder(final int service,
			final String requestName) {
		stringBuilder = new StringBuilder();
		if (requestName != null && !requestName.equals("")) {
			stringBuilder.append(requestName);
			stringBuilder.append(" ");
		}
		switch (service) {
		case LOGIN_ERROR_TAG:
			stringBuilder.append(LOGIN_MESSAGE);
		case LESS_PARAM_TAG:
			stringBuilder.append(LESS_PARAM_MESSAGE);
			break;
		case UNAUTHORIZED_TAG:
			stringBuilder.append(UNAUTHORIZED_MESSAGE);
			break;
		case EMPTY_CLIENT_TAG:
			stringBuilder.append(EMPTY_CLIENT_MESSAGE);
			break;
		case SERVER_ERROR_TAG:
			stringBuilder.append(SERVER_ERROR_MESSAGE);
			break;
		case SUCCESS_TAG:
			stringBuilder.append(SUCCESS_MESSAGE);
			break;
		case SUCCESS_SERVICE_BUT_NO_DATA:
			stringBuilder.append(SUCCESS_SERVICE_BUT_NO_DATA_MESSAGE);
			break;
		}
		return stringBuilder.toString();
	}
}
