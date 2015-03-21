package main.java.com.betelguese.utils;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.betelguese.servlet.BSMService;
import main.java.com.betelguese.utils.helpers.Log;

/**
 * This class is for process either HTTP <code>GET</code> or <code>POST</code>
 * request. The Client app will call a service with a request name. This Class
 * is for find out and process those request.
 * 
 * To use this class follow the code snippet below to create an object of it.
 * <code>
 * HttpRequestProcess httpRequestProcess = new HttpRequestProcess();
 * </code>
 * 
 * If the request is <code>GET</code> then simply call it method below after
 * creating the object of {@link HttpRequestProcess}
 * 
 * <code>
 * requestProcess.getRequestProcess(request, response);
 * </code>
 * 
 * Or if the request is <code>POST</code> then simply call it method below after
 * creating the object of {@link HttpRequestProcess}
 * 
 * <code>
 * requestProcess.postRequestProcess(request, response);
 * </code>
 * 
 * All the request will be processed under
 * {@link HttpRequestProcess#requestProcess(HttpServletRequest, HttpServletResponse)}
 * method.
 * 
 * @author tuman
 * @version 1.0
 *
 */
public class HttpRequestProcess implements RequestName {

	public static final String TAG = HttpRequestProcess.class.getSimpleName();
	private RequestService requestService;

	public HttpRequestProcess() {
		requestService = new RequestService();
	}

	/**
	 * This method will be called usually under the {@link Servlet} Service
	 * Class.For this project it will be {@link BSMService}.when the servlet
	 * receive a <code>GET</code> request it simply calls the
	 * {@link HttpRequestProcess#getRequestProcess(HttpServletRequest, HttpServletResponse)}
	 * .
	 * 
	 * @param request
	 * @param response
	 */
	public String getRequestProcess(HttpServletRequest request,
			HttpServletResponse response) {
		return requestProcess(request, response);
	}

	/**
	 * This method will be called usually under the {@link Servlet} Service
	 * Class.For this project it will be {@link BSMService}.when the servlet
	 * receive a <code>GET</code> request it simply calls the
	 * {@link HttpRequestProcess#postRequestProcess(HttpServletRequest, HttpServletResponse)}
	 * .
	 * 
	 * @param request
	 * @param response
	 */
	public String postRequestProcess(HttpServletRequest request,
			HttpServletResponse response) {
		return requestProcess(request, response);
	}

	/**
	 * This method will be called only under this Class. When the servlet
	 * receive a <code>GET</code> request it simply call
	 * {@link HttpRequestProcess#getRequestProcess(HttpServletRequest, HttpServletResponse)}
	 * or
	 * {@link HttpRequestProcess#postRequestProcess(HttpServletRequest, HttpServletResponse)}
	 * according the request.In this method those request will be finalized.
	 * 
	 * @param request
	 * @param response
	 */
	private String requestProcess(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			final String requestName = request.getParameter(REQUEST_TAG);
			if (requestName.equals(LOG_IN_REQUEST)) {
				Log.d(TAG, LOG_IN_REQUEST
						+ " is called by the client application");
				return requestService.logInRequest(requestName, request);
			} else if (requestName.equals(CREATE_USER_REQUEST)) {
				Log.d(TAG, CREATE_USER_REQUEST
						+ " is called by the client application");
				return null;
			} else if (requestName.equals(SEARCH_REQUEST)) {
				Log.d(TAG, SEARCH_REQUEST 
						+ " is called by the client application");
				return requestService.SearchRequest(requestName, request);
			} else if (requestName.equals(TRANSACTION_REQUEST)) {
				Log.d(TAG, TRANSACTION_REQUEST
						+ " is called by the client application");
				return requestService.transactionRequest(requestName, request);
			} else if (request.equals(UPDATE_REQUEST)) {
				Log.d(TAG, UPDATE_REQUEST
						+ " is called by the client applicationn");
				return requestService.updateRequest(requestName, request);

			} else if (requestName.equals(REPORT_REQUEST)) {
				Log.d(TAG, REPORT_REQUEST
						+ " is called by the client application");
				return requestService.reportRequest(requestName, request);
			} else {
				Log.e(TAG,
						requestName + " is called by the client application",
						new NullPointerException());
				return requestService.unknownClientRequest(requestName);
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "No request was called by the client application", e);
			return requestService.unknownClientRequest(null);
		}
	}
}
