package db;

import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import dao.FindUserDao;
import dao.UserDao;
import model.User;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
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

    public static InsertOneResult addUser(User user) {
        MongoCollection userCollection = database.getCollection("User");
        Document document = new Document("user", user);
        return userCollection.insertOne(document);
    }

    public static List<User> findAll() {
        MongoCollection<FindUserDao> userCollection = database.getCollection("User", FindUserDao.class);
        List<FindUserDao> result = new ArrayList<>();
        userCollection.find().into(result);
        return result.stream()
                .map(u -> u.getUserDao().toEntity())
                .collect(Collectors.toList());
    }
}
