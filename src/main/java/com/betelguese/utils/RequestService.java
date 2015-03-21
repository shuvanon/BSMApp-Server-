package main.java.com.betelguese.utils;

import javax.servlet.http.HttpServletRequest;

import main.java.com.betelguese.utils.RequestName.LogInRequest;
import main.java.com.betelguese.utils.RequestName.ReportRequest;
import main.java.com.betelguese.utils.RequestName.SearchRequest;
import main.java.com.betelguese.utils.RequestName.TransactionRequest;
import main.java.com.betelguese.utils.RequestName.UpdateRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.betelguese.services.LogInService;
import main.java.com.betelguese.services.SearchService;
import main.java.com.betelguese.services.ServiceTag;
import main.java.com.betelguese.services.TransactionService;
import main.java.com.betelguese.services.UpdateService;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.json.builders.Message;

public class RequestService implements LogInRequest, TransactionRequest,
		ReportRequest, SearchRequest, UpdateRequest, ServiceTag {

	private static final String TAG = RequestService.class.getSimpleName();

	public RequestService() {
	}

	public String logInRequest(String requestName, HttpServletRequest request) {

		try {
			final String userName = request.getParameter(USERNAME_PARAM);
			final String password = request.getParameter(PASSWORD_PARAM);
			if (userName == null || password == null) {
				return lessParamClientRequest(requestName);
			} else {
				LogInService logInService = new LogInService();
				return logInService.logInValidity(userName, password);
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "Error to get params", e);
			return lessParamClientRequest(requestName);
		}
	}

	public String SearchRequest(String requestName, HttpServletRequest request) {
		try {
			final String searchKey = request.getParameter(SEARCH_KEY_PARAM);
			final String searchValue = request.getParameter(SEARCH_VALUE_PARAM);
			if (searchKey == null || searchValue == null) {
				return lessParamClientRequest(requestName);
			} else {
				SearchService searchService = new SearchService();
				return searchService.searchBooks(searchKey, searchValue);
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "Error to get params", e);
			return lessParamClientRequest(requestName);
		}
	}

	public String updateRequest(String requestName, HttpServletRequest request) {

		try {
			final String addBooks = request.getParameter(ADD_PARAM);
			final String updateBooks = request.getParameter(UPDATE_PARAM);
			final String maxId = request.getParameter(MAX_ID_PARAM);
			final String updateSearch = request
					.getParameter(UPDATE_SEARCH_PARAM);
			System.out.println(maxId+" as");
			if (addBooks == null && updateBooks == null && updateSearch == null
					& maxId == null) {
				Log.e(TAG, "no parameter got", new NullPointerException(
						"no parameter got."));
				return lessParamClientRequest(requestName);
			} else {
				UpdateService updateService = new UpdateService();
				if (updateBooks == null && addBooks != null
						&& updateSearch == null && maxId == null) {
					System.out.println(addBooks);
					return updateService.updateDatabase(addBooks, updateBooks);
				} else if (updateBooks == null && addBooks == null
						&& maxId == null && updateSearch != null) {
					return updateService.searchAll(updateSearch);
				} else if (updateBooks == null && addBooks == null
						&& updateSearch == null && maxId != null) {
					if (maxId.equals("booksTable")) {
 						return updateService.getMaxId();
					} else {
						return unauthorizeClientRequest(requestName);
					}
				} else {
					return unauthorizeClientRequest(requestName);
				}
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "Error to get params", e);
			return lessParamClientRequest(requestName);
		}
	}

	public String reportRequest(String requestName, HttpServletRequest request) {
		try {
			final String service = request.getParameter(SERVICE_PARAM);
			if (service == null) {
				return lessParamClientRequest(requestName);
			} else {
				// ReportService reportService = new ReportService();
				return null;
				// return reportService.getReport(service);
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "Error to get params", e);
			return lessParamClientRequest(requestName);
		}
	}

	public String transactionRequest(String requestName,
			HttpServletRequest request) {
		try {
			final String serviceKey = request.getParameter(SERVICE_KEY_PARAM);
			final String serviceValue = request
					.getParameter(SERVICE_VALUE_PARAM);
			if (serviceKey == null && serviceValue == null) {
				return lessParamClientRequest(requestName);
			} else {
				TransactionService service = new TransactionService();
				return service.getService(serviceKey, serviceValue);
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "Error to get params", e);
			return lessParamClientRequest(requestName);
		}
	}

	public String unknownClientRequest(String requestName) {
		if (requestName != null && !requestName.trim().equals("")) {
			return unauthorizeClientRequest(requestName);
		} else {
			return emptyClientRequest(requestName);
		}
	}

	private String unauthorizeClientRequest(String requestName) {
		Log.e(TAG, requestName + " is unauthorized.", new Exception(requestName
				+ " is unauthorized."));
		Message message = new Message(0, MessageBuilder.messageBuilder(
				UNAUTHORIZED_SERVICE, requestName));
		Gson gson = new GsonBuilder().create();
		return gson.toJson(message);
	}

	private String emptyClientRequest(String requestName) {
		Log.e(TAG, requestName
				+ " is recieved with no parameter from the client.",
				new Exception(requestName
						+ " is recieved with no parameter from the client."));
		Message message = new Message(0, MessageBuilder.messageBuilder(
				EMPTY_CLIENT_SERVICE, requestName));
		Gson gson = new GsonBuilder().create();
		return gson.toJson(message);
	}

	private String lessParamClientRequest(String requestName) {
		Log.e(TAG, requestName
				+ " is recieved with less parameter from the client.",
				new Exception(requestName
						+ " is recieved with less parameter from the client."));
		Message message = new Message(0, MessageBuilder.messageBuilder(
				LESS_PARAM_SERVICE, requestName));
		Gson gson = new GsonBuilder().create();
		return gson.toJson(message);

	}

}
