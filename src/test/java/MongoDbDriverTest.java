import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 6:23
 */
public class MongoDbDriverTest {
    final String DB_NAME = "testDB";
    final String COLLECTION_NAME = "testCollection";
    final String URI = "mongodb://localhost:27017";

    @Test
    public void mongoDbConnectionTest(){
        // when, then
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            database.getCollection(COLLECTION_NAME);
        }
    }

    @Test
    public void mongoDbInsertTest(){
        // given
        String key = "color";
        String value = "red";
        Document doc1 = new Document(key, value).append("qty", 5);

        MongoClient mongoClient = MongoClients.create(URI);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        MongoCollection collection = database.getCollection(COLLECTION_NAME);

        // when
        InsertOneResult result = collection.insertOne(doc1);

        // then
        assertThat(result.wasAcknowledged()).isTrue();
    }

    @Test
    public void mongoDbReadTest(){
        // given
        MongoClient mongoClient = MongoClients.create(URI);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        MongoCollection collection = database.getCollection(COLLECTION_NAME);

        // when
        FindIterable<Document> findIterable = collection.find(new Document());

        // then
        findIterable.forEach(d -> System.out.println(d));
    }
}
