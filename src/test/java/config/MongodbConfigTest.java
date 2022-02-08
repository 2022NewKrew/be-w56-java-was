package config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class MongodbConfigTest {

    static MongoDatabase database;
    static MongoCollection<Document> collection;
    static final String DOCUMENT_NAME = "java_junit_test";

    @BeforeAll
    public static void init() {
        database = MongodbConfig.getDatabase();
        collection = database.getCollection(DOCUMENT_NAME);
        collection.deleteMany(new Document());
    }

    @AfterAll
    public static void cleanup() {
        collection.drop();
    }

    @BeforeEach
    public void setup() {
        Document document = new Document("setup", "true");
        collection.insertOne(document);
    }

    @AfterEach
    public void teardown() {
        collection.deleteMany(new Document());
    }

    @Test
    public void mongodbFindTest() {
        // when
        Document document = collection.find(new Document("setup", "true")).first();

        // then
        String value = document.get("setup", String.class);
        assertThat(value).isEqualTo("true");
    }

    @Test
    public void mongodbInsertTest() {
        // given
        Document document = new Document();
        document.append("title", "java-was")
                .append("author", "carrot");

        // when
        collection.insertOne(document);

        // then
        long count = collection.countDocuments();
        assertThat(count).isEqualTo(2);
    }

}
