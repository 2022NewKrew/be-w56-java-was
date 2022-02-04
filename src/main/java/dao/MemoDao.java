package dao;

import model.Memo;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemoDao {
    private Connection connection;
    private Statement statement;

    public MemoDao(Connection connection) throws SQLException {
        this.connection = connection;
        init();
    }

    public void init() throws SQLException {
        StringBuilder sb = new StringBuilder();
        statement = connection.createStatement();
        String sql = sb.append("CREATE TABLE IF NOT EXISTS memos(")
                .append("id INT AUTO_INCREMENT PRIMARY KEY,")
                .append("date VARCHAR(16) NOT NULL,")
                .append("writer VARCHAR(16) NOT NULL,")
                .append("context VARCHAR(1024) NOT NULL")
                .append(");").toString();
        statement.execute(sql);
    }

    public void addMemo(Memo memo) throws SQLException {
        String sql = "INSERT INTO memos(date, writer, context) VALUES(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, memo.getDate());
        preparedStatement.setString(2, memo.getWriter());
        preparedStatement.setString(3, memo.getContext());
        preparedStatement.executeUpdate();
    }

    public List<Memo> findAll() throws SQLException {
        List<Memo> memos = new ArrayList<>();
        String sql = String.format("SELECT * FROM memos");
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next())
            memos.add(getMemo(resultSet));
        return memos;
    }

    public Memo getMemo(ResultSet resultSet) throws SQLException {
        String date = resultSet.getString("date");
        String writer = resultSet.getString("writer");
        String context = resultSet.getString("context");
        return new Memo(date, writer, context);
    }

}
