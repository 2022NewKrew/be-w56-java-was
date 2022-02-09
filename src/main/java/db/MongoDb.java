package db;

import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import dao.ArticleDao;
import dao.UserDao;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 7:33
 */
public class MongoDb {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "testDB";
    private static final MongoDatabase database;

    static {
        MongoClient mongoClient = MongoClients.create(URI);
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        database = mongoClient.getDatabase(DB_NAME).withCodecRegistry(pojoCodecRegistry);
    }

    public static InsertOneResult addUser(UserDao userDao) {
        MongoCollection<UserDao> userCollection = database.getCollection("User", UserDao.class);
        return userCollection.insertOne(userDao);
    }

    public static List<UserDao> findAll() {
        MongoCollection<UserDao> userCollection = database.getCollection("User", UserDao.class);
        List<UserDao> result = new ArrayList<>();
        userCollection.find().into(result);
        return result;
    }

    public static UserDao findUserByUserId(String userId) {
        MongoCollection<UserDao> userCollection = database.getCollection("User", UserDao.class);
        FindIterable<UserDao> findIterable = userCollection.find(eq("userId", userId));
        return findIterable.first();
    }

    public static InsertOneResult addArticle(ArticleDao articleDao) {
        MongoCollection<ArticleDao> articleCollection = database.getCollection("Article", ArticleDao.class);
        return articleCollection.insertOne(articleDao);
    }

    public static List<ArticleDao> findAllArticle() {
        MongoCollection<ArticleDao> articleCollection = database.getCollection("Article", ArticleDao.class);
        List<ArticleDao> result = new ArrayList<>();
        articleCollection.find().into(result);
        return result;
    }
}
