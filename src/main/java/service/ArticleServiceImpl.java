package service;

import db.article.ArticleRepository;
import db.article.ArticleRepositoryImpl;
import http.request.Queries;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.exception.ArticleNotFoundException;

import java.util.List;

public class ArticleServiceImpl implements ArticleService{
    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private static final ArticleServiceImpl articleServiceImpl = new ArticleServiceImpl();
    private static final ArticleRepository articleRepository = ArticleRepositoryImpl.getInstance();

    private ArticleServiceImpl(){}

    public static ArticleServiceImpl getInstance(){
        return articleServiceImpl;
    }

    @Override
    public Article createArticle(Queries queries) {
        Article article = new Article(
                queries.get("userId"), queries.get("title"), queries.get("contents")
        );
        articleRepository.addArticle(article);
        log.info("[ARTICLE SERVICE] : " + article);
        return article;
    }

    @Override
    public Article searchArticleByUserId(Queries queries) {
        return articleRepository.findArticleByUserId(queries.get("userId"))
                .orElseThrow(()-> new ArticleNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public List<Article> searchAllArticles() {
        return articleRepository.findAll();
    }
}
