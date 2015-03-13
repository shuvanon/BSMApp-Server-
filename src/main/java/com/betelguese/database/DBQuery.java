package main.java.com.betelguese.database;

import main.java.com.betelguese.database.DBUtils.Column;
import main.java.com.betelguese.database.DBUtils.Keyword;
import main.java.com.betelguese.database.DBUtils.Table;

/**
 * {@link DBQuery} class for execute query according to each client
 * request.Query can be add new item to a table or get data from the table. To
 * use this class there is no need of create an object of it. All the service
 * method is static. SO they can access without it creation of object.
 * 
 *
 * @author tuman
 * @version 1.0
 */
public final class DBQuery implements Table, Column, Keyword {

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return the query to get the profile of the user
	 */
	public static final String getUserProfile(final String userName,
			final String password) {
		final String clientUsername = buildEqualCheckString(userName);
		final String clientPassword = buildEqualCheckString(password);
		return SELECT_ALL + ADMINISTRATION_TABLE + WHERE + USERNAME + EQUAL_TO
				+ clientUsername + AND + PASSWORD + EQUAL_TO + clientPassword
				+ STRING_END;
	}

	/**
	 * 
	 * @param bookId
	 * @return
	 */
	public static final String searchByBooksId(String bookId) {
		return singleAttrSearch(BOOKS_ID, bookId);
	}

	/**
	 * 
	 * @param bookName
	 * @return
	 */
	public static final String searchByBooksName(String bookName) {
		return singleAttrSearch(BOOKS_NAME, bookName);
	}

	/**
	 * 
	 * @param authorName
	 * @return
	 */
	public static final String searchByAuthor(String authorName) {
		return singleAttrSearch(BOOKS_AUTHOR, authorName);
	}

	/**
	 * 
	 * @param publisherName
	 * @return
	 */
	public static final String searchByPublisher(String publisherName) {
		return singleAttrSearch(PUBLISHER_NAME, publisherName);
	}

	/**
	 * 
	 * @param ISBN
	 * @return
	 */
	public static final String searchByISBN(String ISBN) {
		return singleAttrSearch(BOOKS_ISBN, ISBN);
	}

	/**
	 * 
	 * @param shelf
	 * @return
	 */
	public static final String searchByShelf(String shelf) {
		return singleAttrSearch(DISPLAY_SHELF, shelf);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private static final String singleAttrSearch(final String key,
			final String value) {
		return buildSelectString(BOOKS_ID, BOOKS_NAME, PUBLISHER_NAME,
				BOOKS_AUTHOR, BOOKS_ISBN, BOOKS_TOTAL_STOCK, DISPLAY_ID,
				DISPLAY_SHELF, DISPLAY_COLUMN, DISPLAY_ROW, BOOKS_PRICE)
				+ BOOKS_TABLE
				+ NATURAL
				+ JOIN
				+ DISPLAY_TABLE
				+ JOIN
				+ PUBLISHER_TABLE
				+ USING
				+ buildFunctionString(PUBLISHER_ID)
				+ WHERE + key + LIKE + buildLikeCheckString(value) + STRING_END;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static final String updateScreenSearch(final String value) {
		return SELECT_ALL + BOOKS_TABLE + NATURAL + JOIN + DISPLAY_TABLE + JOIN
				+ PUBLISHER_TABLE + USING + buildFunctionString(PUBLISHER_ID)
				+ JOIN + SUPPLIED_BOOKS_TABLE + USING
				+ buildFunctionString(BOOKS_ID) + JOIN + SUPPLY_TABLE + USING
				+ buildFunctionString(SUPPLY_ID) + JOIN + ADMINISTRATION_TABLE
				+ USING + buildFunctionString(ADMINISTRATOR_ID) + WHERE
				+ BOOKS_ID + LIKE + buildEqualCheckString(value) + STRING_END;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static final String searchByAll(final String value) {
		return buildSelectString(BOOKS_ID, BOOKS_NAME, PUBLISHER_NAME,
				BOOKS_AUTHOR, BOOKS_ISBN, BOOKS_TOTAL_STOCK, DISPLAY_ID,
				DISPLAY_SHELF, DISPLAY_COLUMN, DISPLAY_ROW, BOOKS_PRICE)
				+ BOOKS_TABLE
				+ NATURAL
				+ JOIN
				+ DISPLAY_TABLE
				+ JOIN
				+ PUBLISHER_TABLE
				+ USING
				+ buildFunctionString(PUBLISHER_ID)
				+ WHERE
				+ BOOKS_ID
				+ LIKE
				+ buildLikeCheckString(value)
				+ OR
				+ BOOKS_NAME
				+ LIKE
				+ buildLikeCheckString(value)
				+ OR
				+ BOOKS_AUTHOR
				+ LIKE
				+ buildLikeCheckString(value)
				+ OR
				+ BOOKS_ISBN
				+ LIKE
				+ buildLikeCheckString(value)
				+ OR
				+ PUBLISHER_NAME
				+ LIKE
				+ buildLikeCheckString(value)
				+ OR
				+ DISPLAY_SHELF
				+ LIKE
				+ buildLikeCheckString(value)
				+ STRING_END;
	}

	/**
	 * 
	 * @param publisherName
	 * @return
	 */
	public static final String getPublisherIdByName(final String publisherName) {
		return buildSelectString(PUBLISHER_ID) + PUBLISHER_TABLE + WHERE
				+ PUBLISHER_NAME + LIKE + buildEqualCheckString(publisherName)
				+ STRING_END;
	}

	public static String getDisplayId(String displayShelf,
			String displayColumn, String displayRow) {
		// TODO Auto-generated method stub
		return buildSelectString(DISPLAY_ID) + DISPLAY_TABLE + WHERE
				+ DISPLAY_SHELF + EQUAL_TO
				+ buildEqualCheckString(displayShelf) + AND + DISPLAY_COLUMN
				+ EQUAL_TO + buildEqualCheckString(displayColumn) + AND
				+ DISPLAY_ROW + EQUAL_TO + buildEqualCheckString(displayRow)
				+ STRING_END;
	}

	public static String getSupplyID(String totalPrice, String totalPaid,
			String administratorId, String publisherId) {
		// TODO Auto-generated method stub
		return buildSelectString(SUPPLY_ID) + SUPPLY_TABLE + WHERE
				+ TOTAL_PRICE + LIKE + buildEqualCheckString(totalPrice) + AND
				+ TOTAL_PAID + LIKE + buildEqualCheckString(totalPaid) + AND
				+ ADMINISTRATOR_ID + LIKE
				+ buildEqualCheckString(administratorId) + AND + PUBLISHER_ID
				+ LIKE + buildEqualCheckString(publisherId) + STRING_END;
	}

	public static String getBooks(String booksName, String booksAuthor,
			String booksISBN, String publisherId, String individualPrice) {
		return buildSelectString(BOOKS_ID) + BOOKS_TABLE + WHERE + BOOKS_NAME
				+ LIKE + buildEqualCheckString(booksName) + AND + BOOKS_AUTHOR
				+ LIKE + buildEqualCheckString(booksAuthor) + AND + BOOKS_ISBN
				+ LIKE + buildEqualCheckString(booksISBN) + AND + PUBLISHER_ID
				+ LIKE + buildEqualCheckString(publisherId) + AND + BOOKS_PRICE
				+ LIKE + buildEqualCheckString(individualPrice) + STRING_END;
	}

	public static final String insertIntoSupply(final String totalPrice,
			final String totalPaid, final String comment,
			final String administratorId, final String publisherId) {
		return INSERT
				+ SUPPLY_TABLE
				+ buildInsertString(TOTAL_PRICE, TOTAL_PAID, COMMENT,
						ADMINISTRATOR_ID, PUBLISHER_ID)
				+ VALUES
				+ buildInsertValueString(totalPrice, totalPaid, comment,
						administratorId, publisherId) + STRING_END;
	}

	public static String insertSuppliedBookTable(String supplyId,
			String booksId, String publisherId, String booksTotalStock) {
		// TODO Auto-generated method stub
		return INSERT
				+ SUPPLIED_BOOKS_TABLE
				+ buildInsertString(SUPPLY_ID, BOOKS_ID, PUBLISHER_ID, QUANTITY)
				+ VALUES
				+ buildInsertValueString(supplyId, booksId, publisherId,
						booksTotalStock) + STRING_END;
	}

	public static String insertIntoBooksTable(String booksId, String booksName,
			String booksAuthor, String booksISBN, String publisherId,
			String individualPrice, String booksTotalStock, String displayId) {
		// TODO Auto-generated method stub
		return INSERT
				+ BOOKS_TABLE
				+ buildInsertString(BOOKS_ID, BOOKS_NAME, BOOKS_AUTHOR,
						BOOKS_ISBN, PUBLISHER_ID, BOOKS_PRICE,
						BOOKS_TOTAL_STOCK, DISPLAY_ID)
				+ VALUES
				+ buildInsertValueString(booksId, booksName, booksAuthor,
						booksISBN, publisherId, individualPrice,
						booksTotalStock, displayId) + STRING_END;
	}

	public static String insertIntoDisplayTable(String displayShelf,
			String displayColumn, String displayRow) {
		return INSERT
				+ DISPLAY_TABLE
				+ buildInsertString(DISPLAY_SHELF, DISPLAY_COLUMN, DISPLAY_ROW)
				+ VALUES
				+ buildInsertValueString(displayShelf, displayColumn,
						displayRow) + STRING_END;
	}

	public static final String insertIntoPublisherTable(
			final String publisherName) {
		return INSERT + PUBLISHER_TABLE + buildInsertString(PUBLISHER_NAME)
				+ VALUES + buildInsertValueString(publisherName) + STRING_END;
	}

	/**
	 * 
	 * @param userName
	 * @return the query to check is the user name already taken
	 */
	public static final String checkIsUserProfile(final String userName) {
		final String clientUsername = buildLikeCheckString(userName);
		return SELECT_ALL + ADMINISTRATION_TABLE + WHERE + USERNAME + LIKE
				+ clientUsername + STRING_END;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static final String buildLikeCheckString(final String string) {
		return String.format(" '%s%s' ", string, "%");
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static final String buildEqualCheckString(final String string) {
		return String.format(" '%s' ", string);
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static final String buildFunctionString(final String string) {
		return String.format("(" + String.format("%s", string) + ") ", string);
	}

	/**
	 * 
	 * @param selectArgs
	 * @return
	 */
	private static final String buildSelectString(final String... selectArgs) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(SELECT);
		for (int i = 0; i < selectArgs.length; i++) {
			stringBuilder.append(selectArgs[i]);
			if (i < selectArgs.length - 1) {
				stringBuilder.append(COMMA);
			}
		}
		stringBuilder.append(FROM);
		return stringBuilder.toString();
	}

	/**
	 * 
	 * @param selectArgs
	 * @return
	 */
	private static final String buildInsertString(final String... strings) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(");
		for (int i = 0; i < strings.length; i++) {
			stringBuilder.append(String.format("`%s`", strings[i]));
			if (i < strings.length - 1) {
				stringBuilder.append(COMMA);
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	/**
	 * 
	 * @param selectArgs
	 * @return
	 */
	private static final String buildInsertValueString(final String... strings) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(");
		for (int i = 0; i < strings.length; i++) {
			stringBuilder.append(String.format("'%s'", strings[i]));
			if (i < strings.length - 1) {
				stringBuilder.append(COMMA);
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	public static String getMaxBooksId() {
		// TODO Auto-generated method stub
		System.out.println(SELECT + MAX + buildFunctionString(BOOKS_ID) + AS
				+ "maxId" + FROM + BOOKS_TABLE);
		return SELECT + MAX + buildFunctionString(BOOKS_ID) + AS + " "
				+ "maxId" + FROM + BOOKS_TABLE;
	}

	public static String updateBooks(String booksId, String booksTotalStock,
			String displayId) {
		return UPDATE + BOOKS_TABLE + SET + BOOKS_TOTAL_STOCK + EQUAL_TO
				+ buildEqualCheckString(booksTotalStock) + BOOKS_TOTAL_STOCK
				+ DISPLAY_ID + EQUAL_TO + buildEqualCheckString(displayId)
				+ WHERE + BOOKS_ID + EQUAL_TO + buildEqualCheckString(booksId)
				+ STRING_END;
	}

	public static String transactionId(String value) {
		// TODO Auto-generated method stub
		return "SELECT max(transaction_id) as transaction_id FROM transactions WHERE transaction_id like "
				+ "'" + value + "%'";
	}

	public static String customerId(String customerName, String customerNumber) {
		// TODO Auto-generated method stub
		return "select customer_id from customer where customer_name='"
				+ customerName + "' and customer_mobile='" + customerNumber
				+ "'";
	}

	public static String addCustomer(String customerName, String customerNumber) {
		// TODO Auto-generated method stub
		return "insert into customer (customer_name,customer_mobile) values ('"
				+ customerName + "','" + customerNumber + "');";
	}

	public static String createTransaction(String trasactionId,
			String customerId, String adminId, String totalPaid) {
		// TODO Auto-generated method stub
		return "insert into transactions (transaction_id, total_price, administrator_id, customer_id) values('"
				+ trasactionId
				+ "','"
				+ totalPaid
				+ "','"
				+ adminId
				+ "','"
				+ customerId + "');";
	}

	public static String addBooksTransaction(String booksId,
			String trasactionId, String quantity) {
		// TODO Auto-generated method stub
		return "INSERT INTO `sustsa_book_store`.`books_has_transactions` (`books_id`, `transactions_id`, `quantity`, `discount`) VALUES ('"
				+ booksId
				+ "', '"
				+ trasactionId
				+ "', '"
				+ quantity
				+ "', '0');";
	}

	public static String updateTransactedBooks(String booksId, String quantity) {
		// TODO Auto-generated method stub
		return "update books set books_total_stock=books_total_stock-"+quantity+" where books_id=+"+booksId+";";
	}

	public static String getMaxMinYear() {
		// TODO Auto-generated method stub
		return "select max(transactions_date) as max_year,min(transactions_date) as min_year from transactions;";
	}

	public static String getYearTransactions(int year) {
		// TODO Auto-generated method stub
		return "select sum(total_price) as total from transactions where transactions_date like '"+year+"%';";
	}	
}
