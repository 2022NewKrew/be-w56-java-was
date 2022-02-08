package dao;

import model.Article;
import model.User;

import java.sql.SQLException;
import java.util.List;

public interface ArticleDao {
    public void openConnection() throws SQLException, ClassNotFoundException;
    public void closeConnection() throws SQLException;
    public void save(Article article) throws SQLException;
    public Article findById(final int id) throws SQLException;
    public List<Article> findAll() throws SQLException;
    public void delete(final int id) throws SQLException;
}
