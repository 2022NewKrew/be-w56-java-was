package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MyRowMapper<T> {

    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
