package cafe.repository;

import cafe.model.Qna;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QnaRepository {
    private static final String DB_URL = "jdbc:mysql://10.202.174.226/spring_cafe";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root123";

    public void addQna(Qna qna) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            String query = "INSERT INTO QNA(writer, title, contents, created_at) " +
                    "VALUE('" + qna.getWriter() + "', '" + qna.getTitle() + "', '" + qna.getContents() + "', '" + qna.getCreated_at() + "')";

            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Qna> findAll() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            String query = "SELECT writer, title, contents, created_at FROM QNA WHERE deleted = false ORDER BY created_at desc ";

            ResultSet resultSet = statement.executeQuery(query);
            List<Qna> qnaList = new ArrayList<>();

            while (resultSet.next()) {
                qnaList.add(new Qna(resultSet.getString("writer"), resultSet.getString("title"),
                        resultSet.getString("contents"), resultSet.getTimestamp("created_at").toLocalDateTime()));
            }
            return qnaList;
        } catch (SQLException e) {
            return null;
        }
    }
}
