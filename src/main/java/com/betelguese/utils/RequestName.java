package main.java.com.betelguese.utils;

/**
 * This class contains some constant.This constant is the request name which
 * will be called by the client application.
 * 
 * @author tuman
 * @version 1.0
 */
public interface RequestName {

	public static final String REQUEST_TAG = "requestName";
	public static final String LOG_IN_REQUEST = "logInRequest";
	public static final String SEARCH_REQUEST = "searchRequest";
	public static final String TRANSACTION_REQUEST = "transactionRequest";
	public static final String UPDATE_REQUEST = "updateRequest";
	public static final String REPORT_REQUEST = "reportRequest";
	public static final String SETTINGS_REQUEST = "settingsRequest";

	public interface LogInRequest {
		public static final String USERNAME_PARAM = "userName";
		public static final String PASSWORD_PARAM = "password";
	}

	public interface UpdateRequest {
		public static final String ADD_PARAM = "addBooks";
		public static final String UPDATE_PARAM = "updateBooks";
		public static final String MAX_ID_PARAM = "maxId";
		public static final String UPDATE_SEARCH_PARAM = "updateSearch";
	}

	public interface TransactionRequest {
		public static final String SERVICE_KEY_PARAM = "serviceKey";
		public static final String SERVICE_VALUE_PARAM = "serviceValue";
		public static final String GET_BOOKS_SERVICE = "getBooks";
		public static final String ADD_TRANSACTION_SERVICE = "addTransaction";
		public static final String GET_TRANSACTION_ID_SERVICE = "getTransactionId";
	}

	public interface ReportRequest {
		public static final String R_SERVICE_KEY_PARAM = "serviceKey";
		public static final String R_SERVICE_VALUE_PARAM = "serviceValue";
		public static final String YEARLY_SERVICE_KEY = "yearlyService";
		public static final String GET_YEAR_SERVICE_KEY = "getYearService";
		public static final String MONTHLY_SERVICE_KEY = "monthlyService";
		public static final String DAILY_SERVICE_KEY = "dailyService";
		public static final String TRANSACTION_SERVICE_KEY = "transactionService";

	}

	public interface SearchRequest {
		public static final String SEARCH_KEY_PARAM = "searchKey";
		public static final String SEARCH_VALUE_PARAM = "searchValue";
		public static final String SEARCH_BY_BOOK_ID_PARAM = "searchByBookId";
		public static final String SEARCH_BY_BOOK_PARAM = "searchByBook";
		public static final String SEARCH_BY_AUTHOR_PARAM = "searchByAuthor";
		public static final String SEARCH_BY_PUBLISHER_PARAM = "searchByPublisher";
		public static final String SEARCH_BY_ISBN_PARAM = "searchByISBN";
		public static final String SEARCH_BY_SHELF_PARAM = "searchByShelf";
		public static final String SEARCH_BY_ALL_PARAM = "searchByAll";
	}

	public interface SettingsRequest {
		public static final String SERVICE_KEY_PARAM = "serviceKey";
		public static final String CHANGE_NAME_PARAM = "changeUsername";
		public static final String CHANGE_PASSWORD_PARAM = "changePassword";
		public static final String CREATE_USER_PARAM = "createUser";
		public static final String SERVICE_VALUE_PARAM = "serviceValue";
	}
}
