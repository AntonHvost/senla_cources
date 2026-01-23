package database;

import di.annotation.Component;
import di.annotation.Inject;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TransactionManager {

    @Inject
    public TransactionManager() {}

    public void beginTransaction() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            System.out.println("Rolling back");
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
