package db.article;

import model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    void addArticle(Article article);
    Optional<Article> findArticleByUserId(String userId);
    List<Article> findAll();
}
