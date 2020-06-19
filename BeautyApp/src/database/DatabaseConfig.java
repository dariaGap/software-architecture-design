package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static Connection connection;
    private static DatabaseConfig databaseConfig;

    private DatabaseConfig() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/beauty",
                    "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConfig getInstance(){
        if(databaseConfig == null)
            databaseConfig = new DatabaseConfig();
        return databaseConfig;
    }

    public Connection getConnection(){
        return connection;
    }
}
