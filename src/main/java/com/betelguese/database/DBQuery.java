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
		return commonSearch(value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static final String transactionScreenSearch(final String value) {
		return commonSearch(value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static final String commonSearch(final String value) {
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
		System.out.println(buildSelectString(DISPLAY_ID) + DISPLAY_TABLE
				+ WHERE + DISPLAY_SHELF + EQUAL_TO
				+ buildEqualCheckString(displayShelf) + AND + DISPLAY_COLUMN
				+ EQUAL_TO + buildEqualCheckString(displayColumn) + AND
				+ DISPLAY_ROW + EQUAL_TO + buildEqualCheckString(displayRow)
				+ STRING_END);
		return buildSelectString(DISPLAY_ID) + DISPLAY_TABLE + WHERE
				+ DISPLAY_SHELF + EQUAL_TO
				+ buildEqualCheckString(displayShelf) + AND + DISPLAY_COLUMN
				+ EQUAL_TO + buildEqualCheckString(displayColumn) + AND
				+ DISPLAY_ROW + EQUAL_TO + buildEqualCheckString(displayRow)
				+ STRING_END;
	}

	public static String getSupplyID(String totalPrice, String totalPaid,
			String administratorId, String publisherId) {

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
		System.out.println(INSERT
				+ DISPLAY_TABLE
				+ buildInsertString(DISPLAY_SHELF, DISPLAY_COLUMN, DISPLAY_ROW)
				+ VALUES
				+ buildInsertValueString(displayShelf, displayColumn,
						displayRow) + STRING_END);
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
	 * @return
	 */
	public static String getMaxBooksId() {
		return SELECT + MAX + buildFunctionString(BOOKS_ID) + AS + " "
				+ "maxId" + FROM + BOOKS_TABLE + STRING_END;
	}

	/**
	 * 
	 * @param booksId
	 * @param booksTotalStock
	 * @param displayId
	 * @return
	 */
	public static String updateBooks(String booksId, String booksTotalStock,
			String displayId) {
		return UPDATE + BOOKS_TABLE + SET + BOOKS_TOTAL_STOCK + EQUAL_TO
				+ buildEqualCheckString(booksTotalStock) + BOOKS_TOTAL_STOCK
				+ DISPLAY_ID + EQUAL_TO + buildEqualCheckString(displayId)
				+ WHERE + BOOKS_ID + EQUAL_TO + buildEqualCheckString(booksId)
				+ STRING_END;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String transactionId(String value) {
		return SELECT + MAX + buildFunctionString(TRANSACTION_ID)
				+ builsAs(TRANSACTION_ID) + FROM + TRANSACTION_TABLE + WHERE
				+ TRANSACTION_ID + LIKE + "'" + value + "%'" + STRING_END;
	}

	/**
	 * 
	 * @param customerName
	 * @param customerNumber
	 * @return
	 */
	public static String customerId(String customerName, String customerNumber) {
		return buildSelectString(CUSTOMER_ID) + CUSTOMER_TABLE + WHERE
				+ CUSTOMER_NAME + EQUAL_TO
				+ buildEqualCheckString(customerName) + AND + CUSTOMER_MOBILE
				+ EQUAL_TO + buildEqualCheckString(customerNumber) + STRING_END;
	}

	/**
	 * 
	 * @param customerName
	 * @param customerNumber
	 * @return
	 */
	public static String addCustomer(String customerName, String customerNumber) {
		return INSERT + CUSTOMER_TABLE
				+ buildInsertString(CUSTOMER_NAME, CUSTOMER_MOBILE) + VALUES
				+ buildInsertValueString(customerName, customerNumber)
				+ STRING_END;
	}

	/**
	 * 
	 * @param trasactionId
	 * @param customerId
	 * @param adminId
	 * @param totalPaid
	 * @return
	 */
	public static String createTransaction(String trasactionId,
			String customerId, String adminId, String totalPaid) {
		return INSERT
				+ TRANSACTION_TABLE
				+ buildInsertString(TRANSACTION_ID, TOTAL_PRICE,
						ADMINISTRATOR_ID, CUSTOMER_ID)
				+ VALUES
				+ buildInsertValueString(trasactionId, totalPaid, adminId,
						customerId) + STRING_END;
	}

	/**
	 * 
	 * @param booksId
	 * @param trasactionId
	 * @param quantity
	 * @return
	 */
	public static String addBooksTransaction(String booksId,
			String transactionId, String quantity) {
		return INSERT
				+ BOOKS_HAS_TRANSACTION_TABLE
				+ buildInsertString(BOOKS_ID, TRANSACTION_ID, QUANTITY,
						DISCOUNT) + VALUES
				+ buildInsertValueString(booksId, transactionId, quantity, "0")
				+ STRING_END;
	}

	public static String updateTransactedBooks(String booksId, String quantity) {
		return UPDATE + BOOKS_TABLE + SET + BOOKS_TOTAL_STOCK + EQUAL_TO
				+ BOOKS_TOTAL_STOCK + "-" + quantity + " " + WHERE + BOOKS_ID
				+ EQUAL_TO + buildEqualCheckString(booksId) + STRING_END;
	}

	public static String getMaxMinYear() {
		return SELECT + MAX + buildFunctionString(TRANSACTION_DATE) + AS
				+ MAX_YEAR + COMMA + MIN
				+ buildFunctionString(TRANSACTION_DATE) + AS + MIN_YEAR + FROM
				+ TRANSACTION_TABLE + STRING_END;
	}

	public static String getYearTransactions(int year) {
		return SELECT + SUM + buildFunctionString(TOTAL_PRICE) + AS + "y"+year
				+ FROM + TRANSACTION_TABLE + WHERE + TRANSACTION_DATE + LIKE
				+ buildLikeCheckString(Integer.toString(year)) + STRING_END;
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
	private static final String builsAs(final String string) {
		return String.format(AS + " %s", string);
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
		stringBuilder.append(") ");
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
		stringBuilder.append(") ");
		return stringBuilder.toString();
	}
}
