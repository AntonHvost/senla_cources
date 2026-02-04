package database;

import config.ConfigProperty;
import di.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    @ConfigProperty(propertyName = "db.url", type = String.class)
    private static String url;
    @ConfigProperty(propertyName = "db.username", type = String.class)
    private static String user;
    @ConfigProperty(propertyName = "db.password", type = String.class)
    private static String password;

    private Connection connection;

    private ConnectionManager() {}

    private void init () {
        try {
            logger.info("Creating connection");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Не удалось создать соединение с БД", e);
        }
    }
    public Connection getConnection() {
        if(connection == null){
            logger.error("Connection exists! Initialized connect...");
            init();
            //throw new NullPointerException("Подключение отсутствует! Пробуем восстановить соединение...");
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