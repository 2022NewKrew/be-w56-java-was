package db;

import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MyJdbcTemplate {
    public static <T> List<T> query(String query, RowMapper<T> rowMapper) {
        try {
            Connection connection = DataSourceUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = preparedStatement.executeQuery();
            return convert(resultSet, rowMapper);
        }catch (SQLException exception){
            throw new IllegalStateException(exception.getMessage());
        }
    }

    private static <T> List<T> convert(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        if(resultSet.isAfterLast()){
            return Collections.emptyList();
        }

        List<T> results = new ArrayList<>();
        int rowCount = 1;
        while(resultSet.next()){
            results.add(rowMapper.mapRow(resultSet, rowCount++));
        }

        return results;
    }

    public static void update(String query, Object... values){
        try{
            Connection connection = DataSourceUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setObjects(preparedStatement, values);
            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new IllegalStateException(exception.getMessage());
        }
    }

    private static void setObjects(PreparedStatement statement, Object... values) throws SQLException {
        if(values == null){
            return;
        }

        for(int index=0; index < values.length; index++){
            Object value = convertIfNotSupport(values[index]);
            statement.setObject(index+1, value);
        }
    }

    private static Object convertIfNotSupport(Object object){
        if(object instanceof LocalDateTime){
            return Timestamp.valueOf((LocalDateTime) object);
        }

        return object;
    }
}
