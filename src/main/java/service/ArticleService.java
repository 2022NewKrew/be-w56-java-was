package service;

import http.request.Queries;
import model.Article;

import java.util.List;

public interface ArticleService {
    Article createArticle(Queries queries);
    Article searchArticleByUserId(Queries queries);
    List<Article> searchAllArticles();
}
