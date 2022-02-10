package repository;

import db.DataBase;
import model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    public static void addUser(Post post) {
        String sql = "INSERT INTO POST(createdAt, author, contents) VALUES (?, ?, ?)";
        try (PreparedStatement statement = DataBase.getConnection().prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(post.getCreatedAt()));
            statement.setString(2, post.getAuthor());
            statement.setString(3, post.getContents());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        String sql = "SELECT * FROM POST";
        try (Statement statement = DataBase.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                postList.add(new Post(
                        resultSet.getDate("createdAt").toLocalDate(),
                        resultSet.getString("author"),
                        resultSet.getString("contents")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }
}
