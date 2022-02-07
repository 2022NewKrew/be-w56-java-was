package bin.jayden.db;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface MyRowMapper<T> {
    T mapRow(ResultSet resultSet) throws SQLException;
}
