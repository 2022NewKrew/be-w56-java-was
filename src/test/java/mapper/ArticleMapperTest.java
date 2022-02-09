package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dto.ArticleDto;
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
        List<Article> articles = ArticleMapper.INSTANCE.documentsToArticles(documents);
        Article firstArticle = articles.get(0);
        Document firstDocument = documents.get(0);

        assertThat(documents.size()).isEqualTo(articles.size());
        assertThat(firstDocument.getString("author")).isEqualTo(firstArticle.getAuthor());
        assertThat(firstDocument.getString("content")).isEqualTo(firstArticle.getContent());
    }

    private static Stream<List<Document>> getDocuments() {
        List<Document> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            document.put("_id", new ObjectId());
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
        String author = "author";
        String content = "content";
        Article article = new Article(new ObjectId(), author, content);
        Document document = ArticleMapper.INSTANCE.articleToDocument(article);

        assertThat(document.getString("author")).isEqualTo(article.getAuthor());
        assertThat(document.getString("content")).isEqualTo(article.getContent());
    }

    @DisplayName("Document를 Article로 매핑한다.")
    @Test
    void documentToArticle() {
        String author = "author";
        String content = "content";
        Document document = new Document();
        document.putAll(
                Map.of("_id", new ObjectId(), "author", author, "content", content,
                        "createTime",
                        new Date(), "modifiedTime", new Date()));
        Article article = ArticleMapper.INSTANCE.documentToArticle(document);

        assertThat(document.getString("author")).isEqualTo(article.getAuthor());
        assertThat(document.getString("content")).isEqualTo(article.getContent());
    }

    @DisplayName("Article list를 ArticleDto list로 매핑한다.")
    @ParameterizedTest
    @MethodSource("getArticles")
    void articlesToDtos(List<Article> articles) {
        List<ArticleDto> dtos = ArticleMapper.INSTANCE.articlesToDtos(articles);

        Article firstArticle = articles.get(0);
        ArticleDto firstDto = dtos.get(0);

        assertThat(articles.size()).isEqualTo(dtos.size());
        assertThat(firstArticle.getCreateTime()).isEqualTo(firstDto.getCreateTime());
        assertThat(firstArticle.getAuthor()).isEqualTo(firstDto.getAuthor());
        assertThat(firstArticle.getContent()).isEqualTo(firstDto.getContent());
    }

    private static Stream<List<Article>> getArticles() {
        List<Article> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ObjectId id = new ObjectId();
            String author = "author";
            String content = "content";
            result.add(new Article(id, author, content));
        }

        return Stream.of(result);
    }
}
