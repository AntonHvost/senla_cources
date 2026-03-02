package util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DatabaseMigrator {

    private static final String CHANGELOG_PATH = "db/changelog/db.changelog-master.xml";

    private static final ResourceBundle config = ResourceBundle.getBundle("liquibase");

    public static void migrate() {
        Connection connection = null;
        try {
            String url = config.getString("url");
            String user = config.getString("username");
            String password = config.getString("password");

            System.out.println("Подключение к БД: " + url);
            connection = DriverManager.getConnection(url, user, password);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            System.out.println("Применение миграций...");
            liquibase.update();

            System.out.println("Миграции успешно применены!");

            liquibase.close();

        } catch (Exception e) {
            System.err.println("Ошибка миграции: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Не удалось применить миграции", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }
}