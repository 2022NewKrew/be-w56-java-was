package dao;

import dao.connection.ConnectionMaker;
import model.Article;
import model.User;

import java.sql.*;
import java.util.ArrayList;
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
        String sql = String.format("SELECT * from %s where %s = \"%d\"",
                ArticleDBConstants.TABLE_NAME,
                ArticleDBConstants.COLUMN_ID,
                id);

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        if (rs.next()) {
            return new Article(
                    rs.getString(ArticleDBConstants.COLUMN_TITLE),
                    rs.getString(ArticleDBConstants.COLUMN_DATETIME),
                    rs.getString(ArticleDBConstants.COLUMN_WRITER)
            );
        } else {
            return null;
        }
    }

    @Override
    public List<Article> findAll() throws SQLException {
        List<Article> articleList = new ArrayList<>();
        String sql = String.format("Select * from %s", ArticleDBConstants.TABLE_NAME);

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            articleList.add(new Article(
                    rs.getString(ArticleDBConstants.COLUMN_TITLE),
                    rs.getString(ArticleDBConstants.COLUMN_DATETIME),
                    rs.getString(ArticleDBConstants.COLUMN_WRITER)
            ));
        }

        return articleList;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = \"%d\"",
                ArticleDBConstants.TABLE_NAME,
                ArticleDBConstants.COLUMN_ID,
                id);

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
}
