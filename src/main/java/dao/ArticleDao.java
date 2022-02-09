package dao;

import model.Article;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

/**
 * Created by melodist
 * Date: 2022-02-09 009
 * Time: 오전 11:14
 */
public class ArticleDao {

    @BsonProperty("user")
    private UserDao userDao;
    private LocalDateTime createdDate;
    private String content;

    public ArticleDao() {
    }

    public ArticleDao(UserDao userDao, LocalDateTime createdDate, String content) {
        this.userDao = userDao;
        this.createdDate = createdDate;
        this.content = content;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article toEntity() {
        return new Article(userDao.toEntity(), createdDate, content);
    }
}
