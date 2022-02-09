package db;

import com.mongodb.client.result.InsertOneResult;
import dao.ArticleDao;
import dao.UserDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 8:42
 */
class MongoDbTest {

    @Test
    public void addUserTest(){
        // given
        UserDao userDao = new UserDao("test", "test", "test", "test");

        // when
        InsertOneResult result = MongoDb.addUser(userDao);

        // then
        assertThat(result.wasAcknowledged()).isTrue();
    }

    @Test
    public void findAllTest(){
        // given
        String userId = "test";
        UserDao userDao = new UserDao(userId, "test", "test", "test");
        MongoDb.addUser(userDao);

        // when
        List<UserDao> result = MongoDb.findAll();

        // then
        assertThat(result).anyMatch(u -> u.getUserId().equals("test"));
        assertThat(result).anyMatch(u -> u.getPassword().equals("test"));
        assertThat(result).anyMatch(u -> u.getName().equals("test"));
        assertThat(result).anyMatch(u -> u.getEmail().equals("test"));
    }

    @Test
    public void findUserByUserIdTest(){
        // given
        String userId = "test";
        UserDao userDao = new UserDao(userId, "test", "test", "test");
        MongoDb.addUser(userDao);

        // when
        UserDao findUserDao = MongoDb.findUserByUserId(userId);

        // then
        assertThat(findUserDao).usingRecursiveComparison().isEqualTo(userDao);
    }

    @Test
    public void addArticleTest(){
        // given
        String userId = "test";
        UserDao userDao = new UserDao(userId, "test", "test", "test");
        String content = "test";
        ArticleDao articleDao = new ArticleDao(userDao, LocalDateTime.now(), content);

        // when
        InsertOneResult result = MongoDb.addArticle(articleDao);

        // then
        assertThat(result.wasAcknowledged()).isTrue();
    }

    @Test
    public void findAllArticleTest(){
        // given
        String userId = "test";
        String content = "test";
        LocalDateTime createdTime = LocalDateTime.of(2022, 2, 9, 18, 30);
        UserDao userDao = new UserDao(userId, "test", "test", "test");
        ArticleDao articleDao = new ArticleDao(userDao, createdTime, content);
        MongoDb.addArticle(articleDao);

        // when
        List<ArticleDao> articles = MongoDb.findAllArticle();

        // then
        assertThat(articles).anyMatch(u -> u.getUserDao() instanceof UserDao);
        assertThat(articles).anyMatch(u -> u.getCreatedDate().equals(createdTime));
        assertThat(articles).anyMatch(u -> u.getContent().equals(content));
    }
}
