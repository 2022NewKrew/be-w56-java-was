package db;

import mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseConnector {
    private static final String url = "jdbc:mysql://eroica-mysql.ay1.krane.9rum.cc/was?characterEncoding=utf8";
    private static final String username = "eroica_was";
    private static final String password = "!Was1";

    private DatabaseConnector() {

    }

    private static class DatabaseConnectorHelper {
        private static final DatabaseConnector INSTANCE = new DatabaseConnector();
    }

    public static DatabaseConnector getInstance() {
        return DatabaseConnectorHelper.INSTANCE;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int index = 0; index < params.length; index++) {
            if (params[index].getClass() == String.class) {
                preparedStatement.setString(index + 1, (String) params[index]);
                continue;
            }

            if (params[index].getClass() == Long.class) {
                preparedStatement.setLong(index + 1, (Long) params[index]);
                continue;
            }

            if (params[index].getClass() == Integer.class) {
                preparedStatement.setInt(index + 1, (Integer) params[index]);
                continue;
            }

            throw new SQLException("NOT VALID SQL TYPE");
        }
        return preparedStatement;
    }

    public <T> Optional<T> query(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = getPreparedStatement(connection, sql, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        Optional<T> result = Optional.empty();
        if (resultSet.next()) {
            result = rowMapper.mapRow(resultSet);
        }
        closeResources(connection, preparedStatement, resultSet);
        return result;
    }

    public <T> List<T> queryAll(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = getPreparedStatement(connection, sql, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet).get());
        }
        closeResources(connection, preparedStatement, resultSet);
        return results;
    }

    public void update(String sql, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = getPreparedStatement(connection, sql, params);
        preparedStatement.executeUpdate();
        closeResources(connection, preparedStatement, null);
    }

    private void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        if (connection != null) {
            connection.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (resultSet != null) {
            resultSet.close();
        }
    }
}
