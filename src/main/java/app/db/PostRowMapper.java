package app.db;

import lib.was.db.RowMapper;
import lib.was.di.Bean;
import domain.model.Post;
import domain.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Bean
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post map(ResultSet rs) throws SQLException {
        return new Post.Builder()
                .id(rs.getInt("posts.id"))
                .title(rs.getString("posts.title"))
                .content(rs.getString("posts.content"))
                .createdAt(rs.getTimestamp("posts.createdAt").toLocalDateTime())
                .author(
                        new User(
                                rs.getInt("users.id"),
                                rs.getString("users.userId"),
                                rs.getString("users.password"),
                                rs.getString("users.name"),
                                rs.getString("users.email")
                        )
                )
                .build();
    }
}
