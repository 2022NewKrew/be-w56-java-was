package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface RowMapper<T> {
    Optional<T> mapRow(ResultSet resultSet) throws SQLException;
}
