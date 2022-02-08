package dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import mapper.ArticleMapper;
import model.Article;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class ArticleDao implements CrudDao<Article, ObjectId> {

    private static ArticleDao instance;
    private static final ArticleMapper articlemapper = ArticleMapper.instance;

    public static ArticleDao getInstance() {
        if (instance == null) {
            instance = new ArticleDao();
        }
        return instance;
    }

    private final MongoCollection<Document> collection;

    public ArticleDao() {
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase("was");
        this.collection = database.getCollection("Article");
    }

    @Override
    public Article find(ObjectId id) {
        Document document = collection.find(eq("_id", id)).first();
        System.out.println();
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
        return document.getObjectId("_id");
    }

    @Override
    public void update(Article entity) {
        Bson updatePassword = set("title", entity.getTitle());
        Bson updateName = set("author", entity.getAuthor());
        Bson updateEmail = set("content", entity.getContent());
        Bson combineBson = combine(updatePassword, updateName, updateEmail);
        collection.findOneAndUpdate(eq("_id", entity.getId()), combineBson);
    }

    @Override
    public void delete(Article entity) {
        collection.deleteOne(eq("_id", entity.getId()));
    }
}
