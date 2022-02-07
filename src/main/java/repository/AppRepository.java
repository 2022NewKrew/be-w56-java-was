package repository;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Memo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AppRepository {

    private final MongoClient mongo = new MongoClient("localhost", 27017);
    private final MongoDatabase db;
    private final MongoCollection<Document> memoCollection;

    public AppRepository() {
        this.db = mongo.getDatabase("onboarding");
        this.memoCollection = db.getCollection("memo");
    }

    public void create(Memo memo) {// TODO: 저장 로직 짜기
        Document document = new Document("test", "test1");
        memoCollection.insertOne(document);
    }

    public List<Memo> findAll() {
        List<Memo> results = new ArrayList<>();

        memoCollection.find().forEach(
                (Consumer<Document>) x -> results.add(new Memo((String) x.get("name"), (String) x.get("content")))
        );
        System.out.println(results);
        return results;

    }
}
