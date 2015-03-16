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
import main.java.com.betelguese.utils.RequestName.SearchRequest;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.SearchResult;
import main.java.com.betelguese.utils.json.builders.SearchMessage;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;

public class SearchService implements SearchRequest, ServiceTag {

	/**
	 * TAG to identify the class from log.
	 */
	private static final String TAG = SearchService.class.getSimpleName();
	/**
	 * TAG for the client receive service JSON.
	 */
	private static final String REQUEST_NAME = RequestName.SEARCH_REQUEST;

	private DatabaseService databaseService;
	private ResultSet resultSet;

	public SearchService() {

	}

	public String searchBooks(String key, String value) {
		databaseService = new DatabaseService();

		try {
			if (key.equals(SEARCH_BY_BOOK_ID_PARAM)) {
				return searchByBookId(value);
			} else if (key.equals(SEARCH_BY_BOOK_PARAM)) {
				return searchByBookName(value);
			} else if (key.equals(SEARCH_BY_AUTHOR_PARAM)) {
				return searchByAuthor(value);
			} else if (key.equals(SEARCH_BY_PUBLISHER_PARAM)) {
				return searchByPublisher(value);
			} else if (key.equals(SEARCH_BY_SHELF_PARAM)) {
				return searchByShelf(value);
			} else if (key.equals(SEARCH_BY_ISBN_PARAM)) {
				return searchByISBN(value);
			} else if (key.equals(SEARCH_BY_ALL_PARAM)) {
				return searchByAll(value);
			} else {
				return searchError(new NullPointerException(
						"Search wasn't valid."));
			}
		} catch (InstantiationException e) {
			return searchError(e);
		} catch (IllegalAccessException e) {
			return searchError(e);

		} catch (ClassNotFoundException e) {
			return searchError(e);

		} catch (SQLException e) {
			return searchError(e);
		}

	}

	private String searchByAuthor(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByAuthor(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByBookId(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByBooksId(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByBookName(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByBooksName(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByPublisher(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByPublisher(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByISBN(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByISBN(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByShelf(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByShelf(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String searchByAll(String searchString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		databaseService.open();
		resultSet = databaseService.executeQuery(DBQuery
				.searchByAll(searchString));
		databaseService.close();
		return processResultSet(resultSet);
	}

	private String processResultSet(ResultSet resultSet) {
		try {
			if (resultSet.next()) {
				SearchMessage searchMessage = new SearchMessage(1,
						MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				searchMessage.setSearchResult(createSearchResult(resultSet));
				Gson gson = new GsonBuilder().create();
				return gson.toJson(searchMessage);
			} else {
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
		List<SearchResult> searchResult = new ArrayList<SearchResult>();
		SearchResult result;
		do {

			result = new SearchResult();
			result.setBooksId(resultSet.getString("books_id"));
			result.setBooksName(resultSet.getString("books_name"));
			result.setPublisherName(resultSet.getString("publisher_name"));
			result.setBooksISBN(resultSet.getString("books_isbn"));
			result.setBooksAuthor(resultSet.getString("books_author"));
			result.setBooksTotalStock(resultSet.getString("books_total_stock"));
			result.setPrice(resultSet.getString("individual_price"));
			result.setDisplayId(resultSet.getString("display_id"));
			result.setDisplayShelf(resultSet.getString("display_shelf"));
			result.setDisplayColumn(resultSet.getString("display_column"));
			result.setDisplayRow(resultSet.getString("display_row"));
			searchResult.add(result);
		} while (resultSet.next());
		return searchResult;
	}

	private String searchError(Exception e) {
		Log.e(TAG, e.getClass().getSimpleName()
				+ " occured.Search operation was not successful.", e);
		ServiceMessage serviceMessage = new ServiceMessage(0, REQUEST_NAME,
				MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME));
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}
}
