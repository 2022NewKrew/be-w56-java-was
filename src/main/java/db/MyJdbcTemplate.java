package db;

import repository.mapper.MyRowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//TODO : Connection Pool 구현
public class MyJdbcTemplate {

    private Connection conn;

    public MyJdbcTemplate() {
    }

    private void open() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://10.202.179.65:3306/cafe",
                    "root",
                    "1234"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int update(MyPreparedStatementCreator preparedStatementCreator, MyKeyHolder keyHolder) throws SQLException {
        open();
        PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(conn);

        int ret = preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();

        if (rs.next()) {
            keyHolder.setKey(rs.getInt(1));
        }
        close();
        return ret;
    }

    public <T> List<T> query(String sql, MyRowMapper<T> rowMapper) throws SQLException {
        open();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        ResultSet rs = preparedStatement.executeQuery();

        List<T> list = new ArrayList<>();

        while (rs.next()) {
            list.add(rowMapper.mapRow(rs, rs.getRow()));
        }
        close();
        return list;
    }

    public <T> T queryGetObject(String sql, MyRowMapper<T> rowMapper) throws SQLException {
        open();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        ResultSet rs = preparedStatement.executeQuery();

        T ret = null;
        if (rs.next()) {
            ret = rowMapper.mapRow(rs, rs.getRow());
        }

        return ret;
    }
}
