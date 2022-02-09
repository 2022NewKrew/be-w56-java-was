package dao;

import dao.connection.ConnectionMaker;
import model.Article;

import java.sql.*;
import java.util.List;

public class ArticleDaoImpl implements ArticleDao {
    private ConnectionMaker connectionMaker;
    private Connection connection;

    public ArticleDaoImpl(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
        this.connection = connectionMaker.getConnection();
    }

    @Override
    public void save(Article article) throws SQLException {
        String sql = String.format("insert into %s (%s, %s, %s) values (?,?,?)",
                ArticleDBConstants.TABLE_NAME,
                ArticleDBConstants.COLUMN_TITLE,
                ArticleDBConstants.COLUMN_DATETIME,
                ArticleDBConstants.COLUMN_WRITER);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(ArticleDBConstants.COLUMN_INDEX_TITLE, article.getTitle());
        preparedStatement.setTimestamp(ArticleDBConstants.COLUMN_INDEX_DATETIME, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setString(ArticleDBConstants.COLUMN_INDEX_WRITER, article.getWriter());

        int count = preparedStatement.executeUpdate();

        if (count == 0) {
            throw new SQLException("SQL Insertion failed");
        }

        preparedStatement.close();
    }

    @Override
    public Article findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Article> findAll() throws SQLException {
        return null;
    }

    @Override
    public void delete(int id) throws SQLException {

    }
}
