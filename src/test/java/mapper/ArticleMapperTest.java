package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import model.Article;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ArticleMapperTest {

    @DisplayName("document list를 Article List로 매핑한다.")
    @ParameterizedTest
    @MethodSource("getDocuments")
    void documentsToArticles(List<Document> documents) {
        List<Article> articles = ArticleMapper.instance.documentsToArticles(documents);
        Article firstArticle = articles.get(0);
        Document firstDocument = documents.get(0);

        assertThat(documents.size()).isEqualTo(articles.size());
        assertThat(firstDocument.getString("title")).isEqualTo(firstArticle.getTitle());
        assertThat(firstDocument.getString("author")).isEqualTo(firstArticle.getAuthor());
        assertThat(firstDocument.getString("content")).isEqualTo(firstArticle.getContent());
    }

    private static Stream<List<Document>> getDocuments() {
        List<Document> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            document.put("title", "title" + i);
            document.put("author", "author" + i);
            document.put("content", "content" + i);
            document.put("createTime", new Date());
            document.put("modifiedTime", new Date());
            result.add(document);
        }

        return Stream.of(result);
    }

    @DisplayName("Article을 Document로 매핑한다.")
    @Test
    void articleToDocument() {
        String title = "title";
        String author = "author";
        String content = "content";
        Article article = new Article(new ObjectId(), title, author, content);
        Document document = ArticleMapper.instance.articleToDocument(article);

        assertThat(document.getString("title")).isEqualTo(article.getTitle());
        assertThat(document.getString("author")).isEqualTo(article.getAuthor());
        assertThat(document.getString("content")).isEqualTo(article.getContent());
    }

    @DisplayName("Document를 Article로 매핑한다.")
    @Test
    void documentToArticle() {
        String title = "title";
        String author = "author";
        String content = "content";
        Document document = new Document();
        document.putAll(Map.of("title", title, "author", author, "content", content, "createTime",
                new Date(), "modifiedTime", new Date()));
        Article article = ArticleMapper.instance.documentToArticle(document);

        assertThat(document.getString("title")).isEqualTo(article.getTitle());
        assertThat(document.getString("author")).isEqualTo(article.getAuthor());
        assertThat(document.getString("content")).isEqualTo(article.getContent());
    }
}
