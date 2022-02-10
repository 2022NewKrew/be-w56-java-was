package db.mapper;


import webserver.domain.entity.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String writer = rs.getString("writer");
        String content = rs.getString("content");
        LocalDateTime written = rs.getTimestamp("written").toLocalDateTime();

        return new Post(id, writer, content, written);
    }
}