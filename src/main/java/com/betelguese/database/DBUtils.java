package main.java.com.betelguese.database;

interface DBUtils {

	public interface Schema {
		public static final String DATABASE = "bsmapp";
	}

	public interface Table {
		public static final String ADMINISTRATION_TABLE = Schema.DATABASE + "."
				+ "administrator";
		public static final String BOOKS_TABLE = Schema.DATABASE + "."
				+ "books";
		public static final String DISPLAY_TABLE = Schema.DATABASE + "."
				+ "display";
		public static final String PUBLISHER_TABLE = Schema.DATABASE + "."
				+ "publisher";
		public static final String SUPPLY_TABLE = Schema.DATABASE + "."
				+ "supply";
		public static final String SUPPLIED_BOOKS_TABLE = Schema.DATABASE + "."
				+ "supplied_books";

		public static final String TRANSACTION_TABLE = Schema.DATABASE + "."
				+ "transactions";

		public static final String BOOKS_HAS_TRANSACTION_TABLE = Schema.DATABASE
				+ "." + "books_has_transactions";
		public static final String CUSTOMER_TABLE = Schema.DATABASE + "."
				+ "customer";
	}

	public interface Column {

		// administrator table column
		public static final String ADMINISTRATOR_ID = "administrator_id";
		public static final String USERNAME = "user_name";
		public static final String PASSWORD = "password";

		// books table column
		public static final String BOOKS_ID = "books_id";
		public static final String BOOKS_NAME = "books_name";
		public static final String BOOKS_AUTHOR = "books_author";
		public static final String BOOKS_ISBN = "books_isbn";
		public static final String BOOKS_PRICE = "individual_price";
		public static final String BOOKS_TOTAL_STOCK = "books_total_stock";
		public static final String BOOKS_LAST_UPDATE = "books_last_update";

		// display table column
		public static final String DISPLAY_ID = "display_id";
		public static final String DISPLAY_SHELF = "display_shelf";
		public static final String DISPLAY_COLUMN = "display_column";
		public static final String DISPLAY_ROW = "display_row";

		// publisher table column
		public static final String PUBLISHER_ID = "publisher_id";
		public static final String PUBLISHER_NAME = "publisher_name";

		// supply table column
		public static final String SUPPLY_ID = "supply_id";
		public static final String TOTAL_PRICE = "total_price";
		public static final String TOTAL_PAID = "total_paid";
		public static final String COMMENT = "comment";

		//
		public static final String QUANTITY = "quantity";

		// transaction table column
		public static final String TRANSACTION_ID = "transactions_id";
		public static final String TRANSACTION_DATE = "transactions_date";

		// customer table column
		public static final String CUSTOMER_ID = "customer_id";
		public static final String CUSTOMER_NAME = "customer_name";
		public static final String CUSTOMER_MOBILE = "customer_mobile";

		// books_has_transaction table column
		public static final String DISCOUNT = "discount";

		// column changed name
		public static final String MAX_YEAR = "max_year";
		public static final String MIN_YEAR = "min_year";

	}

	public interface Keyword {

		public static final String UPDATE = "UPDATE ";
		public static final String SELECT = "SELECT ";
		public static final String MAX = " MAX";
		public static final String MIN = " MIN";
		public static final String AS = " AS ";
		public static final String FROM = " FROM ";
		public static final String ASTERICKS = "*";
		public static final String SELECT_ALL = SELECT + ASTERICKS + FROM;
		public static final String INSERT = "INSERT INTO ";
		public static final String VALUES = " VALUES ";
		public static final String WHERE = " WHERE ";
		public static final String LIKE = " LIKE ";
		public static final String NATURAL = " NATURAL ";
		public static final String JOIN = " JOIN ";
		public static final String USING = " USING";
		public static final String COUNT = " COUNT";
		public static final String SUM = " SUM";
		public static final String HAVING = " HAVING";
		public static final String EQUAL_TO = " = ";
		public static final String AND = " AND ";
		public static final String SET = " SET ";
		public static final String OR = " OR ";
		public static final String STRING_END = " ;";
		public static final String COLUMN = " COLUMN ";
		public static final String TABLE = " TABLE ";
		public static final String COMMA = ", ";
	}

}
