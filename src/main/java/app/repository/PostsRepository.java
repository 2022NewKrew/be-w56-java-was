package app.repository;

import app.model.Post;
import app.model.Posts;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostsRepository {
    private final Connection connection;

    public PostsRepository(Connection connection) {
        this.connection = connection;
    }


    public long insert(Post post) throws SQLException {
        String insertSql = "insert into POSTS (created_date, writer, content) values (?,?,?);";
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        insertStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        insertStatement.setString(2, post.getWriter());
        insertStatement.setString(3, post.getContent());
        insertStatement.execute();

        String maxIdSql = "select id from POSTS order by id desc limit 1;";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxIdSql);
        ResultSet resultSet = maxIdStatement.executeQuery();

        if(!resultSet.next())
            throw new SQLException("데이터를 읽어오지 못했습니다!");

        return resultSet.getInt(1);
    }

    public Post findById(long curId) throws SQLException {
        String findByIdSql = "select created_date, id, writer, content from POSTS where id = ?";
        PreparedStatement findByIdStatement = connection.prepareStatement(findByIdSql);
        findByIdStatement.setLong(1, curId);
        ResultSet resultSet = findByIdStatement.executeQuery();

        if(!resultSet.next())
            throw new SQLException("데이터를 읽어오지 못했습니다!");

        return new Post(
                resultSet.getTimestamp(1).toLocalDateTime(),
                resultSet.getLong(2),
                resultSet.getString(3),
                resultSet.getString(4)
        );
    }

    public Posts findAll() throws SQLException {
        String findAllSql = "select created_date, id, writer, content from POSTS order by created_date desc limit 10;";
        PreparedStatement findAllStatement = connection.prepareStatement(findAllSql);
        ResultSet resultSet = findAllStatement.executeQuery();
        List<Post> posts = new ArrayList<>();
        while(resultSet.next()){
            posts.add(
                    new Post(
                            resultSet.getTimestamp(1).toLocalDateTime(),
                            resultSet.getLong(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return new Posts(posts);
    }

}
