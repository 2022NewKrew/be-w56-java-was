package bin.jayden.db;

import bin.jayden.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyJDBC {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //드라이버
    private final String DB_URL = "jdbc:mysql://jayden-bin-onbaording.ay1.krane.9rum.cc:3306/kakaodb2"; //접속할 DB 서버

    private final String USER_NAME = "jayden"; //DB에 접속할 사용자 이름을 상수로 정의
    private final String PASSWORD = "1Q2w3e4r!"; //사용자의 비밀번호를 상수로 정의
    private Connection conn = null;

    public MyJDBC() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int update(String sql) {
        try {
            Statement state = conn.createStatement();
            return state.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException();
        }
    }

    public <T> List<T> queryObjectList(String sql, MyRowMapper<T> rowMapper) {
        List<T> resultList = new ArrayList<>();
        try {
            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery(sql);
            while (resultSet.next()) {
                resultList.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException();
        }
        return resultList;
    }

    public <T> T queryObject(String sql, MyRowMapper<T> rowMapper) {
        List<T> resultList = queryObjectList(sql, rowMapper);
        if (resultList.size() == 0)
            return null;
        else
            return resultList.get(0);
    }
}
