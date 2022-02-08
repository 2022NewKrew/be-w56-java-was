package repository;

import com.mongodb.client.MongoCollection;
import config.MongodbConfig;
import model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final MongoCollection<Document> collection = MongodbConfig.getCollection("user");

    public void save(User user) {
        Document document = new Document();
        document.append("userId", user.getUserId())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("email", user.getEmail());

        collection.insertOne(document);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document document : collection.find()) {
            String userId = document.get("userId", String.class);
            String password = document.get("password", String.class);
            String name = document.get("name", String.class);
            String email = document.get("email", String.class);
            User user = new User(userId, password, name, email);

            users.add(user);
        }

        return users;
    }
}
