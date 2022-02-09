package dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.time.LocalDateTime;
import java.util.List;
import mapper.ArticleMapper;
import model.Article;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class ArticleDao implements CrudDao<Article, ObjectId> {

    private static final ArticleDao INSTANCE = new ArticleDao();
    private static final ArticleMapper articlemapper = ArticleMapper.INSTANCE;

    public static ArticleDao getInstance() {
        return INSTANCE;
    }

    private final MongoCollection<Document> collection;

    public ArticleDao() {
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase(DaoConstant.DATABASE_NAME);
        this.collection = database.getCollection(DaoConstant.ARTICLE_COLLECTION_NAME);
    }

    @Override
    public Article find(ObjectId id) {
        Document document = collection.find(eq(ArticleAttribute.ID.getValue(), id)).first();
        return articlemapper.documentToArticle(document);
    }

    @Override
    public List<Article> find() {
        List<Document> documents = Lists.newArrayList(collection.find());
        return articlemapper.documentsToArticles(documents);
    }

    @Override
    public ObjectId save(Article entity) {
        Document document = articlemapper.articleToDocument(entity);
        collection.insertOne(document);
        return document.getObjectId(ArticleAttribute.ID.getValue());
    }

    @Override
    public void update(Article entity) {
        Bson updateAuthor = set(ArticleAttribute.AUTHOR.getValue(), entity.getAuthor());
        Bson updateContent = set(ArticleAttribute.CONTENT.getValue(), entity.getContent());
        Bson updateModifiedDate = set(ArticleAttribute.MODIFIED_TIME.getValue(), LocalDateTime.now());
        Bson combineBson = combine(updateAuthor, updateContent, updateModifiedDate);
        collection.findOneAndUpdate(eq("_id", entity.getId()), combineBson);
    }

    @Override
    public void delete(Article entity) {
        collection.deleteOne(eq(ArticleAttribute.ID.getValue(), entity.getId()));
    }
}
