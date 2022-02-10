package app.repository;

import app.configure.DbConfigure;
import app.core.annotation.Autowired;
import app.model.Post;
import app.model.Posts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostsRepository {
    private final Connection connection;

    @Autowired
    public PostsRepository(DbConfigure dbConfigure) {
        this.connection = dbConfigure.getConnection();
    }

    public long insert(Post post) throws SQLException {
        String insertSql = "insert into POSTS (writer, content) values (?,?);";
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        insertStatement.setString(1, post.getWriter());
        insertStatement.setString(2, post.getContent());
        insertStatement.execute();

        String maxIdSql = "select id from POSTS order by id desc limit 1;";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxIdSql);
        ResultSet resultSet = maxIdStatement.executeQuery();

        if (!resultSet.next())
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

    // Todo 너무 많은 레코드는 터져버리고 있다... 해결방법을 찾아야함.
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
