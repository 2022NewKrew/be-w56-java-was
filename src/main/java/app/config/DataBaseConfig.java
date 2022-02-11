package app.config;

import static util.Constant.DATABASE_NAME;
import static util.Constant.DATABASE_URL;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DataBaseConfig {
    private static final MongoClient MONGO_CLIENT = MongoClients.create(DATABASE_URL);

    private DataBaseConfig() {
    }

    public static MongoDatabase getDatabase() {
        return MONGO_CLIENT.getDatabase(DATABASE_NAME);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return MONGO_CLIENT.getDatabase(DATABASE_NAME).getCollection(collectionName);
    }
}
