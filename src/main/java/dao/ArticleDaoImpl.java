package dao;

import model.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ArticleDaoImpl implements ArticleDao {
    private Connection connection;

    @Override
    public void save(Article article) throws SQLException {

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
