package mapper;

import java.util.List;
import model.Article;
import org.bson.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleMapper {

    ArticleMapper instance = Mappers.getMapper(ArticleMapper.class);

    List<Article> documentsToArticles(List<Document> documents);

    default Document articleToDocument(Article article) {
        if (article == null) {
            return null;
        }
        Document document = new Document();
        document.put("_id", article.getId());
        document.put("title", article.getTitle());
        document.put("author", article.getAuthor());
        document.put("content", article.getContent());
        return document;
    }

    default Article documentToArticle(Document document) {
        if (document == null) {
            return null;
        }

        return new Article(
                document.getObjectId("_id"),
                document.getString("title"),
                document.getString("author"),
                document.getString("content"));
    }
}
