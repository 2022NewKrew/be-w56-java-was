package bin.jayden.repository;

import bin.jayden.db.MyJDBC;
import bin.jayden.db.MyRowMapper;
import bin.jayden.model.Article;

import java.util.List;

public class ArticleRepository {
    private final MyJDBC jdbc;

    public ArticleRepository(MyJDBC jdbc) {
        this.jdbc = jdbc;
    }

    public List<Article> getArticleList() {
        return jdbc.queryObjectList("SELECT A.id, title, writerID,name, A.time from Article A join User U on U.id = A.writerID", getArticleRowMapper());
    }

    private MyRowMapper<Article> getArticleRowMapper() {
        return resultSet -> new Article(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getLong("writerID"),
                resultSet.getString("name"),
                resultSet.getString("time"));
    }

    public int createArticle(Article article) {
        return jdbc.update("insert into Article(title, writerID) VALUES ('" + article.getTitle() + "'," + article.getWriterId() + ")");
    }
}
