package main.java.com.betelguese.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.betelguese.database.DBQuery;
import main.java.com.betelguese.database.DatabaseService;
import main.java.com.betelguese.utils.MessageBuilder;
import main.java.com.betelguese.utils.RequestName;
import main.java.com.betelguese.utils.RequestName.TransactionRequest;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.SearchResult;
import main.java.com.betelguese.utils.items.TransactionBook;
import main.java.com.betelguese.utils.items.TransactionInfo;
import main.java.com.betelguese.utils.json.builders.SearchMessage;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;
import main.java.com.betelguese.utils.json.builders.TransactionIdMessage;

public class TransactionService implements ServiceTag, TransactionRequest {
	private DatabaseService databaseService;
	private ResultSet resultSet;

	private static final String TAG = TransactionService.class.getSimpleName();
	private static final String REQUEST_NAME = RequestName.TRANSACTION_REQUEST;

	public TransactionService() {
	}

	public String getService(String key, String value) {
		databaseService = new DatabaseService();
		if (key.equals(GET_BOOKS_SERVICE)) {
			return getBooks(value);
		} else if (key.equals(ADD_TRANSACTION_SERVICE)) {
			return addTransaction(value);
		} else if (key.equals(GET_TRANSACTION_ID_SERVICE)) {
			return getTransactionId(value);
		}
		databaseService.close();
		return null;
	}

	private String addTransaction(String string) {
		try {
			// {requestName=transactionRequest,
			// serviceValue={"customerName":"Sajid","customerNumber":"01680842208",
			// "transactionId":"150320001","totalPaid":"280","adminId":"1",
			// "transactionBooks":[{"booksId":"1","quantity":"2"}]},
			// serviceKey=addTransaction}.
			Gson gson = new GsonBuilder().create();
			TransactionInfo transactionInfo = gson.fromJson(string,
					TransactionInfo.class);
			createTransactionInfo(transactionInfo);
			databaseService.open();
			String transactionId = transactionInfo.getTransactionId();
			List<TransactionBook> transactionBooks = transactionInfo
					.getTransactionBooks();
			for (int i = 0; i < transactionBooks.size(); i++) {
				TransactionBook transactionBook = transactionBooks.get(i);
				final String booksId = transactionBook.getBooksId();
				final String quantity = transactionBook.getQuantity();
				createBookTransaction(booksId, transactionId, quantity);
				updateBooks(booksId, quantity);

			}
			return serviceSuccesful();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (SQLException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		}
	}

	private String serviceSuccesful() {
		ServiceMessage serviceMessage = new ServiceMessage(1,
				MessageBuilder.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

	private void updateBooks(String booksId, String quantity) {
		databaseService.execute(DBQuery
				.updateTransactedBooks(booksId, quantity));
	}

	private void createBookTransaction(String booksId, String trasactionId,
			String quantity) {
		databaseService.execute(DBQuery.addBooksTransaction(booksId,
				trasactionId, quantity));
	}

	private void createTransactionInfo(TransactionInfo transactionInfo) {
		String customerName = transactionInfo.getCustomerName();
		String customerNumber = transactionInfo.getCustomerNumber();
		String transactionId = transactionInfo.getTransactionId();
		String totalPaid = transactionInfo.getTotalPaid();
		String adminId = transactionInfo.getAdminId();
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery.customerId(
					customerName, customerNumber));
			if (!resultSet.next()) {
				databaseService.execute(DBQuery.addCustomer(customerName,
						customerNumber));
				resultSet = databaseService.executeQuery(DBQuery.customerId(
						customerName, customerNumber));
				resultSet.next();
			}
			String customerId = resultSet.getString("customer_id");
			databaseService.execute(DBQuery.createTransaction(transactionId,
					customerId, adminId, totalPaid));
			databaseService.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private String getTransactionId(String value) {
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery
					.transactionId(value));
			databaseService.close();
			return processIdResultSet(resultSet, value);
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

	private String processIdResultSet(ResultSet resultSet, String value) {

		try {
			if (resultSet.next()) {
				final String transactionId = resultSet
						.getString("transaction_id") == null ? value + "000"
						: resultSet.getString("transaction_id");
				return createTransactionId(transactionId);
			} else {
				System.out.println(value + "000");
				return createTransactionId(value + "000");
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.search was not successful.", e);
			return serverErrorMessage(e);
		}
	}

	private String createTransactionId(String transactionId) {
		TransactionIdMessage transactionIdMessage = new TransactionIdMessage(1,
				MessageBuilder.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
				REQUEST_NAME);
		transactionIdMessage.setTransactionId(transactionId);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(transactionIdMessage);
	}

	private String getBooks(String value) {
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery
					.updateScreenSearch(value));
			databaseService.close();
			return processResultSet(resultSet);
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

	private String serverErrorMessage(Exception e) {
		Log.e(TAG, e.getClass().getSimpleName()
				+ " occured.search was not successful.", e);
		ServiceMessage serviceMessage = new ServiceMessage(0,
				MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

	private String processResultSet(ResultSet resultSet) {
		try {
			if (resultSet.next()) {
				Log.d(TAG, "Search was successful.");
				SearchMessage searchMessage = new SearchMessage(1,
						MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				searchMessage.setSearchResult(createSearchResult(resultSet));
				Gson gson = new GsonBuilder().create();
				return gson.toJson(searchMessage);
			} else {
				Log.d(TAG, "No book was found.");
				ServiceMessage serviceMessage = new ServiceMessage(0,
						MessageBuilder.messageBuilder(
								SUCCESS_SERVICE_BUT_NO_DATA, REQUEST_NAME),
						REQUEST_NAME);
				Gson gson = new GsonBuilder().create();
				return gson.toJson(serviceMessage);
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getClass().getSimpleName()
					+ " occured.search was not successful.", e);
			ServiceMessage serviceMessage = new ServiceMessage(0,
					MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME),
					REQUEST_NAME);
			Gson gson = new GsonBuilder().create();
			return gson.toJson(serviceMessage);
		}
	}

	private List<SearchResult> createSearchResult(ResultSet resultSet)
			throws SQLException {
		/*
		 * books_id,books_name,publisher_name,books_isbn,books_author,
		 * books_total_stock
		 */
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		SearchResult searchResult;
		do {
			searchResult = new SearchResult();
			searchResult.setBooksId(resultSet.getString("books_id"));
			searchResult.setBooksName(resultSet.getString("books_name"));
			searchResult.setPublisherName(resultSet.getString("books_name"));
			searchResult.setBooksTotalStock(resultSet
					.getString("books_total_stock"));
			searchResult.setPrice(resultSet.getString("individual_price"));
			searchResult.setBooksAuthor(resultSet.getString("books_author"));
			searchResults.add(searchResult);
		} while (resultSet.next());
		return searchResults;
	}
}
