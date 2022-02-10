package mapper;

import model.Memo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MemoRowMapper implements RowMapper<Memo> {
    @Override
    public Optional<Memo> mapRow(ResultSet resultSet) throws SQLException {
        return Optional.of(Memo.builder()
                .writer(resultSet.getString("writer"))
                .content(resultSet.getString("content"))
                .date(resultSet.getString("date").substring(0, 10))
                .build());
    }
}
