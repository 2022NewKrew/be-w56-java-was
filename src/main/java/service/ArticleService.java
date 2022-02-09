package service;

import dao.ArticleDao;
import dao.UserDao;
import db.MongoDb;
import model.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by melodist
 * Date: 2022-02-09 009
 * Time: 오전 11:13
 */
public class ArticleService {
    public static void createArticle(String userId, String contents) {
        UserDao userDao = MongoDb.findUserByUserId(userId);
        ArticleDao articleDao = new ArticleDao(userDao, LocalDateTime.now(), contents);
        MongoDb.addArticle(articleDao);
    }

    public static List<Article> getArticleList() {
        List<ArticleDao> articleDaoList = MongoDb.findAllArticle();
        return articleDaoList.stream()
                .map(ArticleDao::toEntity)
                .collect(Collectors.toList());
    }
}
