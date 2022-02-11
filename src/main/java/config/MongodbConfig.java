package config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public abstract class MongodbConfig {

    private static final String DB_NAME = "local";
    private static final String DB_HOST = System.getenv("MONGODB_HOST");
    private static final MongoClient MONGO_CLIENT = new MongoClient(DB_HOST, 27017);

    private MongodbConfig() {
    }

    public static MongoDatabase getDatabase() {
        return MONGO_CLIENT.getDatabase(DB_NAME);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return MONGO_CLIENT.getDatabase(DB_NAME).getCollection(collectionName);
    }
}
