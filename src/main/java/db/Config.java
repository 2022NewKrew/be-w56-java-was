package db;

import java.sql.Connection;

public class Config {

    private Config() {
    }

    public static Connection getDBConnection() {
        return databaseConnection();
    }

    private static Connection databaseConnection() {
        /*JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:java-was-db");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;*/
        return null;
    }
}
