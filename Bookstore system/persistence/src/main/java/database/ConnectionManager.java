package database;

import config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
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
            logger.info("Creating connection");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Не удалось создать соединение с БД", e);
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    logger.info("Inializing ConnectionManager");
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if(connection == null){
            logger.error("Connection exists!");
            throw new NullPointerException("Подключение отсутствует!");
        }
        try {
            if (connection.isClosed()) {
                logger.error("Connection is closed!");
                throw new SQLException("Подключение закрыто!");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("Getting connection");
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    logger.info("Closing connection");
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}