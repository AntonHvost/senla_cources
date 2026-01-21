package database;

import config.ConfigProperty;

public final class DBConstant {
    public static final String TABLE_BOOKS = "book";
    public static final String TABLE_CONSUMERS = "consumer";
    public static final String TABLE_ORDERS = "order";
    public static final String TABLE_ORDER_ITEMS = "order_item";
    public static final String TABLE_BOOK_REQUESTS = "book_request";

    private DBConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
