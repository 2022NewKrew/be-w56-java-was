package mapper;

import dto.ArticleDto;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import model.Article;
import org.bson.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    List<Article> documentsToArticles(List<Document> documents);

    List<ArticleDto> articlesToDtos(List<Article> articles);

    default Document articleToDocument(Article article) {
        if (article == null) {
            return null;
        }
        Document document = new Document();
        document.put("_id", article.getId());
        document.put("author", article.getAuthor());
        document.put("content", article.getContent());
        document.put("createTime", article.getCreateTime());
        document.put("modifiedTime", article.getModifiedTime());
        return document;
    }

    default Article documentToArticle(Document document) {
        if (document == null) {
            return null;
        }

        return new Article(
                document.getObjectId("_id"),
                document.getString("author"),
                document.getString("content"),
                Instant.ofEpochMilli(document.getDate("createTime").getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                Instant.ofEpochMilli(document.getDate("modifiedTime").getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
