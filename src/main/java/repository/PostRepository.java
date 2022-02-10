package repository;

import db.DataBase;
import model.Post;

import java.sql.*;

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

}
