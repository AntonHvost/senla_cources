package database;

import config.ConfigProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    @ConfigProperty(propertyName = "db.url", type = String.class)
    private static String url;
    @ConfigProperty(propertyName = "db.username", type = String.class)
    private static String user;
    @ConfigProperty(propertyName = "db.password", type = String.class)
    private static String password;

    private static volatile ConnectionManager instance;
    private final Connection connection;

    private ConnectionManager() {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать соединение с БД", e);
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if(connection == null){
            throw new NullPointerException("Подключение отсутствует!");
        }
        try {
            if (connection.isClosed()) {
                throw new SQLException("Подключение закрыто!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}