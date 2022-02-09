package dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import model.Article;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleDaoTest {

    @DisplayName("모든 Article의 목록을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100, 1000})
    void testFind(int numberOfArticle) {
        //give
        ArticleDao dao = ArticleDao.getInstance();
        int baseSize = dao.find().size();

        List<ObjectId> ids = new ArrayList<>();
        for (int i = 0; i < numberOfArticle; i++) {
            ObjectId id = new ObjectId();
            String author = "author" + i;
            String content = "content" + i;
            Article article = new Article(id, author, content);
            ids.add(dao.save(article));
        }

        //when
        List<Article> articles = dao.find();

        //then
        assertThat(articles.size()).isEqualTo(numberOfArticle + baseSize);

        for (ObjectId id : ids) {
            dao.delete(id);
        }
    }

    @DisplayName("Article을 저장, 조회, 삭제를 테스트한다.")
    @Test
    void saveAndFindAndDelete() {
        //give
        ArticleDao dao = ArticleDao.getInstance();
        String author = "author";
        String content = "content";
        Article article = new Article(new ObjectId(), author, content);
        ObjectId id = dao.save(article);
        //when
        Article newArticle = dao.find(id);

        //then
        assertThat(newArticle.getId()).isEqualTo(id);
        assertThat(newArticle.getAuthor()).isEqualTo(author);
        assertThat(newArticle.getContent()).isEqualTo(content);

        dao.delete(id);

        Article nullArticle = dao.find(id);
        assertThat(nullArticle).isNull();
    }

    @DisplayName("입력받은 Entity의 title, author, content 값으로 데이터베이스의 Article을 수정한다.")
    @Test
    void update() {
        //give
        ArticleDao dao = ArticleDao.getInstance();
        String author = "testAuthor";
        String content = "testContent";
        Article article = new Article(new ObjectId(), author, content);
        ObjectId id = dao.save(article);

        String newAuthor = "newAuthor";
        String newContent = "newContent";

        //when
        dao.update(new Article(article.getId(), newAuthor, newContent));
        Article updateArticle = dao.find(article.getId());

        //then
        assertThat(updateArticle.getAuthor()).isEqualTo(newAuthor);
        assertThat(updateArticle.getContent()).isEqualTo(newContent);

        dao.delete(id);
    }
}
