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
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
