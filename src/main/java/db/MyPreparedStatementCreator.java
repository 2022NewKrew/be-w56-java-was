package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface MyPreparedStatementCreator {
    PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
