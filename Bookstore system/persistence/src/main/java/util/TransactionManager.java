package util;

import di.annotation.Component;
import di.annotation.Inject;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TransactionManager {

    private final ConnectionManager connectionManager;
    private Connection connection;

    @Inject
    public TransactionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void beginTransaction() {
        this.connection = connectionManager.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            System.out.println("Rolling back");
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
