package dao.connection;

import java.sql.Connection;

public interface ConnectionMaker {
    Connection getConnection();
}
