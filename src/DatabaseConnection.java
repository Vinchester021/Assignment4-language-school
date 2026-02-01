package db;

import exception.DatabaseOperationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/LanguageSchoolDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "GGtemir099!";

    private static Connection connection;
    private static boolean printed = false;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                if (!printed) {
                    System.out.println("DB connected: " + URL);
                    printed = true;
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB connection failed", e);
        }
    }
}


