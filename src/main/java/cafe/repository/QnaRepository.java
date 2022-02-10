package cafe.repository;

import cafe.model.Qna;
import cafe.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QnaRepository {
    public void addQna(Qna qna) {
        String query = "INSERT INTO QNA(writer, title, contents, created_at) VALUE(?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, qna.getWriter());
            preparedStatement.setString(2, qna.getTitle());
            preparedStatement.setString(3, qna.getContents());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(qna.getCreatedAt()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Qna> findAll() {
        String query = "SELECT writer, title, contents, created_at FROM QNA WHERE deleted = false ORDER BY created_at desc ";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
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
