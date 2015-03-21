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
import main.java.com.betelguese.utils.RequestName.UpdateRequest;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.UpdateBook;
import main.java.com.betelguese.utils.items.UpdateItems;
import main.java.com.betelguese.utils.items.UpdateSearchResult;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;
import main.java.com.betelguese.utils.json.builders.UpdateBookIdMessage;
import main.java.com.betelguese.utils.json.builders.UpdateMessage;
import main.java.com.betelguese.utils.json.builders.UpdateSearchMessage;

public class UpdateService implements ServiceTag, UpdateRequest {

	private static final String TAG = UpdateService.class.getSimpleName();
	private static final String REQUEST_NAME = RequestName.UPDATE_REQUEST;

	private DatabaseService databaseService;
	private ResultSet resultSet;

	public UpdateService() {

	}

	public String updateDatabase(final String addBooks, final String updateBooks) {
		databaseService = new DatabaseService();
		UpdateMessage updateMessage = new UpdateMessage(1,
				MessageBuilder.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
				REQUEST_NAME);
		try {
			if (addBooks != null) {
				Log.d(TAG, "new books add request.");
				updateMessage.setAddBookMessage(addBooks(addBooks));
			}
			if (updateBooks != null) {
				updateMessage.setAddBookMessage(updateBooks(updateBooks));
			}
		} catch (InstantiationException e) {
			return serverErrorMessage(e);
		} catch (IllegalAccessException e) {
			return serverErrorMessage(e);
		} catch (ClassNotFoundException e) {
			return serverErrorMessage(e);
		} catch (SQLException e) {
			return serverErrorMessage(e);
		}
		Gson gson = new GsonBuilder().create();
		return gson.toJson(updateMessage);
	}

	private String addBooks(String string) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		databaseService.open();
		Gson gson = new GsonBuilder().create();
		UpdateItems updateItems = gson.fromJson(string, UpdateItems.class);
		// {"addBooks":[{"publisherName":"tuman","totalPrice":1000,"totalPaid":1000,
		// "comment":"ok done job","administratorId":2,"displayShelf":1,"displayColumn":1,
		// "displayRow":10,"booksName":"tuman","booksAuthor":"tuman","booksISBN":901,
		// "individualPrice":100,"booksTotalStock":10}]}

		for (UpdateBook updateBook : updateItems.getAddBooks()) {

			// getting the publisher ID.
			String publisherId = getPublisherId(updateBook.getPublisherName());
			// adding the supply
			String supplyId = getSupplyId(updateBook.getTotalPaid(),
					updateBook.getTotalPaid(), updateBook.getComment(),
					updateBook.getAdministratorId(), publisherId);
			// create e new display id or get the old display id
			String displayId = getdisplayId(updateBook.getDisplayShelf(),
					updateBook.getDisplayColumn(), updateBook.getDisplayRow());
			// getting the publisher ID.
			String booksId = getbooksId(updateBook.getBooksId(),
					updateBook.getBooksName(), updateBook.getBooksAuthor(),
					updateBook.getBooksISBN(), publisherId,
					updateBook.getIndividualPrice(),
					updateBook.getBooksTotalStock(), displayId);
			addSuppliedBooks(supplyId, booksId, publisherId,
					updateBook.getBooksTotalStock());
		}
		return "successfull";
	}

	private void addSuppliedBooks(String supplyId, String booksId,
			String publisherId, String booksTotalStock) {
		databaseService.execute(DBQuery.insertSuppliedBookTable(supplyId,
				booksId, publisherId, booksTotalStock));
	}

	private String getbooksId(String booksId, String booksName,
			String booksAuthor, String booksISBN, String publisherId,
			String individualPrice, String booksTotalStock, String displayId)
			throws SQLException {
		resultSet = databaseService.executeQuery(DBQuery.getBooks(booksName,
				booksAuthor, booksISBN, publisherId, individualPrice));
		if (!resultSet.next()) {
			databaseService.execute(DBQuery.insertIntoBooksTable(booksId,
					booksName, booksAuthor, booksISBN, publisherId,
					individualPrice, booksTotalStock, displayId));
			resultSet = databaseService.executeQuery(DBQuery.getBooks(
					booksName, booksAuthor, booksISBN, publisherId,
					individualPrice));
			resultSet.next();
		} else {
			updateBooks(resultSet.getString("books_id"), booksTotalStock,
					displayId);
			resultSet.next();
		}
		return resultSet.getString("books_id");
	}

	private void updateBooks(String booksId, String booksTotalStock,
			String displayId) {
		resultSet = databaseService.executeQuery(DBQuery.updateBooks(booksId,
				booksTotalStock, displayId));
	}

	private String getdisplayId(String displayShelf, String displayColumn,
			String displayRow) throws SQLException {
		resultSet = databaseService.executeQuery(DBQuery.getDisplayId(
				displayShelf, displayColumn, displayRow));
		if (!resultSet.next()) {
			databaseService.execute(DBQuery.insertIntoDisplayTable(
					displayShelf, displayColumn, displayRow));
			resultSet = databaseService.executeQuery(DBQuery.getDisplayId(
					displayShelf, displayColumn, displayRow));
			resultSet.next();
		}
		return resultSet.getString("display_id");
	}

	private String updateBooks(String string) {
		return null;

	}

	private String getPublisherId(final String publisherName)
			throws SQLException {

		resultSet = databaseService.executeQuery(DBQuery
				.getPublisherIdByName(publisherName));
		if (!resultSet.next()) {
			System.out.println(DBQuery.insertIntoPublisherTable(publisherName));
			databaseService.execute(DBQuery
					.insertIntoPublisherTable(publisherName));
			resultSet = databaseService.executeQuery(DBQuery
					.getPublisherIdByName(publisherName));
			resultSet.next();
		}
		return resultSet.getString("publisher_id");
	}

	private String getSupplyId(final String totalPrice, final String totalPaid,
			final String comment, final String administratorId,
			final String publisherId) throws SQLException {

		// "total_price", "total_paid", "comment", "administrator_id",
		// "publisher_id"
		System.out.println(DBQuery.insertIntoSupply(totalPrice, totalPaid,
				comment, administratorId, publisherId));
		databaseService.execute(DBQuery.insertIntoSupply(totalPrice, totalPaid,
				comment, administratorId, publisherId));
		resultSet = databaseService.executeQuery(DBQuery.getSupplyID(
				totalPrice, totalPaid, administratorId, publisherId));
		resultSet.next();
		return resultSet.getString("supply_id");
	}

	public String searchAll(String updateSearch) {
		databaseService = new DatabaseService();
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery
					.updateScreenSearch(updateSearch));
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

	private String processResultSet(ResultSet resultSet) {
		try {
			if (resultSet.next()) {
				UpdateSearchMessage updateSearchMessage = new UpdateSearchMessage(
						1, MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				updateSearchMessage
						.setSearchResult(createSearchResult(resultSet));
				Gson gson = new GsonBuilder().create();
				return gson.toJson(updateSearchMessage);
			} else {
				ServiceMessage serviceMessage = new ServiceMessage(0,
						MessageBuilder.messageBuilder(
								SUCCESS_SERVICE_BUT_NO_DATA, REQUEST_NAME),
						REQUEST_NAME);
				Gson gson = new GsonBuilder().create();
				return gson.toJson(serviceMessage);
			}
		} catch (SQLException e) {
			return serverErrorMessage(e);
		}
	}

	private List<UpdateSearchResult> createSearchResult(ResultSet resultSet)
			throws SQLException {
		List<UpdateSearchResult> updateSearchResults = new ArrayList<UpdateSearchResult>();
		do {
			/*
			 * books_id,books_name,publisher_name,books_isbn,books_author,
			 * books_total_stock,display_id,
			 * display_shelf,display_column,display_row
			 */
			UpdateSearchResult updateSearchResult = new UpdateSearchResult();

			updateSearchResult.setBooksId(resultSet.getString("books_id"));
			updateSearchResult.setBooksName(resultSet.getString("books_name"));
			updateSearchResult.setPublisherName(resultSet
					.getString("publisher_name"));
			updateSearchResult.setBooksISBN(resultSet.getString("books_isbn"));
			updateSearchResult.setBooksAuthor(resultSet
					.getString("books_author"));
			updateSearchResult.setBooksTotalStock(resultSet
					.getString("books_total_stock"));
			updateSearchResult
					.setPrice(resultSet.getString("individual_price"));
			updateSearchResult.setDisplayId(resultSet.getString("display_id"));
			updateSearchResult.setDisplayShelf(resultSet
					.getString("display_shelf"));
			updateSearchResult.setDisplayColumn(resultSet
					.getString("display_column"));
			updateSearchResult
					.setDisplayRow(resultSet.getString("display_row"));
			updateSearchResult.setComment(resultSet.getString("comment"));
			updateSearchResult.setTotalPaid(resultSet.getString("total_paid"));
			updateSearchResult.setAdminName(resultSet
					.getString("administrator_name"));
			updateSearchResult.setQuantity(resultSet.getString("quantity"));
			updateSearchResults.add(updateSearchResult);
		} while (resultSet.next());
		return updateSearchResults;
	}

	public String getMaxId() {
		databaseService = new DatabaseService();
		try {
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery.getMaxBooksId());
			databaseService.close();
			if (resultSet.next()) {
				UpdateBookIdMessage updateBookIdMessage = new UpdateBookIdMessage(
						1, MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME,
						resultSet.getString("maxId"));
				Gson gson = new GsonBuilder().create();
				return gson.toJson(updateBookIdMessage);
			} else {
				UpdateBookIdMessage updateBookIdMessage = new UpdateBookIdMessage(
						1, MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				Gson gson = new GsonBuilder().create();
				return gson.toJson(updateBookIdMessage);
			}
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
}
