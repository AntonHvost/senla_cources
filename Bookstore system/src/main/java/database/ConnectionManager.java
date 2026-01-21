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
            throw new RuntimeException("❌ Не удалось создать соединение с БД", e);
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
        System.out.println(connection);
        if(connection == null){
            throw new NullPointerException("Connection object is null");
        }
        try {
            if (connection.isClosed()) {
                throw new SQLException("Connection has been closed");
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
/*
package database;

import config.ConfigProperty;
import di.annotation.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {

    private static volatile ConnectionManager instance;
    private Connection connection;

    @ConfigProperty(propertyName = "db.url", type = String.class)
    private String url;

    @ConfigProperty(propertyName = "db.username", type = String.class)
    private String user;

    @ConfigProperty(propertyName = "db.password", type = String.class)
    private String password;

    private ConnectionManager() {
        
    }


    public void init() {
        if (connection != null) {
            return;
        }

        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Подключение к БД установлено: " + url);

        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка инициализации соединения с БД", e);
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("ConnectionManager не инициализирован! Вызовите init().");
        }
        try {
            if (connection.isClosed()) {
                throw new IllegalStateException("Соединение с БД закрыто. Перезапустите приложение.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки состояния соединения", e);
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("🔌 Соединение с БД закрыто");
                }
            } catch (SQLException e) {
                System.err.println("⚠️ Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}*/