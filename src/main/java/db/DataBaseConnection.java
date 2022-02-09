package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DataBaseConnection.class);

    public Connection connection() {
        try {
            return DriverManager.getConnection("jdbc:h2:mem:test_mem", "sa", "");
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
        }

        return null;
    }
}
