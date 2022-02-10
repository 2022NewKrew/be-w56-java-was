package dao;

import model.Article;
import model.User;

import java.sql.SQLException;
import java.util.List;

public interface ArticleDao {
    public void save(Article article);
    public Article findById(final int id);
    public List<Article> findAll();
    public void delete(final int id);
}
