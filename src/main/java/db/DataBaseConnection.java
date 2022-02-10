package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DataBaseConnection.class);

    static {
        try (Connection connection = connection();){
            String sql = "create table USER_ACCOUNT (" +
                    "user_account_id bigint auto_increment, " +
                    "user_id varchar(100), " +
                    "password varchar(100), " +
                    "email varchar(100), " +
                    "name varchar(100), " +
                    "primary key (user_account_id)" +
                    ")";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Connection connection() throws SQLException {

        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
    }

}
