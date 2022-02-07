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

public class UserDao implements CrudDao<User, String> {

    private static UserDao instance;
    private static final UserMapper userMapper = UserMapper.instance;

    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private final MongoCollection<Document> collection;

    public UserDao() {
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase("was");
        this.collection = database.getCollection("User");
    }

    @Override
    public User find(String id) {
        Document document = collection
                .find(eq("userId", id))
                .first();
        return userMapper.documentToUser(document);
    }

    @Override
    public List<User> find() {
        List<Document> documents = Lists.newArrayList(collection.find());
        return userMapper.documentsToUsers(documents);
    }

    @Override
    public String save(User entity) {
        String id = entity.getUserId();
        collection.insertOne(userMapper.userToDocument(entity));

        return id;
    }

    @Override
    public void update(User entity) {
        Bson updatePassword = set("password", entity.getPassword());
        Bson updateName = set("name", entity.getName());
        Bson updateEmail = set("email", entity.getEmail());
        Bson combineBson = combine(updatePassword, updateName, updateEmail);
        collection.findOneAndUpdate(eq("userId", entity.getUserId()), combineBson);
    }

    @Override
    public void delete(User entity) {
        Document document = userMapper.userToDocument(entity);
        collection.deleteOne(eq("userId", entity.getUserId()));
    }
}
