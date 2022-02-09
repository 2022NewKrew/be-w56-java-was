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
import mapper.UserMapper;
import model.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class UserDao implements CrudDao<User, ObjectId> {

    private static final UserDao INSTANCE = new UserDao();
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public static synchronized UserDao getInstance() {
        return INSTANCE;
    }

    private final MongoCollection<Document> collection;

    public UserDao() {
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase(DaoConstant.DATABASE_NAME);
        this.collection = database.getCollection(DaoConstant.USER_COLLECTION_NAME);
    }

    @Override
    public User find(ObjectId id) {
        Document document = collection
                .find(eq(UserAttribute.ID.getValue(), id))
                .first();
        return userMapper.documentToUser(document);
    }

    @Override
    public List<User> find() {
        List<Document> documents = Lists.newArrayList(collection.find());
        return userMapper.documentsToUsers(documents);
    }

    @Override
    public ObjectId save(User entity) {
        Document document = userMapper.userToDocument(entity);
        collection.insertOne(document);
        return document.getObjectId(UserAttribute.ID.getValue());
    }

    @Override
    public void update(User entity) {
        Bson updatePassword = set(UserAttribute.PASSWORD.getValue(), entity.getPassword());
        Bson updateName = set(UserAttribute.NAME.getValue(), entity.getName());
        Bson updateEmail = set(UserAttribute.EMAIL.getValue(), entity.getEmail());
        Bson combineBson = combine(updatePassword, updateName, updateEmail);
        collection.findOneAndUpdate(eq(UserAttribute.ID.getValue(), entity.getId()),
                combineBson);
    }

    public void delete(ObjectId id) {
        collection.deleteOne(eq(UserAttribute.ID.getValue(), id));
    }

    public User findByUserId(String userId) {
        Document document = collection
                .find(eq(UserAttribute.USER_ID.getValue(), userId))
                .first();
        return userMapper.documentToUser(document);
    }
}
