package repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mapper.MemoDocumentMapper;
import mapper.MemoDocumentMapperImpl;
import model.Memo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AppRepository {

    private static final MemoDocumentMapper MEMO_DOCUMENT_MAPPER = new MemoDocumentMapperImpl();
    private final MongoClient mongo = new MongoClient("localhost", 27017);
    private final MongoDatabase db;
    private final MongoCollection<Document> memoCollection;

    public AppRepository() {
        this.db = mongo.getDatabase("onboarding");
        this.memoCollection = db.getCollection("memo");
    }

    public void create(Memo memo) {
        Document document = MEMO_DOCUMENT_MAPPER.toRightObject(memo);
        memoCollection.insertOne(document);
    }

    public List<Memo> findAll() {
        List<Memo> results = new ArrayList<>();

        memoCollection.find().forEach(
                (Consumer<Document>) x -> results.add(MEMO_DOCUMENT_MAPPER.toLeftObject(x))
        );
        System.out.println(results);
        return results;

    }
}
