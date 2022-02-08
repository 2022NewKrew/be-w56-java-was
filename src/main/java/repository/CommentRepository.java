package repository;

import com.mongodb.client.MongoCollection;
import config.MongodbConfig;
import model.Comment;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private final MongoCollection<Document> collection = MongodbConfig.getCollection("comment");

    public void save(Comment comment) {
        Document document = new Document();
        document.append("registerDate", comment.getRegisterDate())
                .append("writer", comment.getWriter())
                .append("comment", comment.getComment());

        collection.insertOne(document);
    }

    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        for (Document document : collection.find()) {
            String registerDate = document.get("registerDate", String.class);
            String writer = document.get("writer", String.class);
            String comment = document.get("comment", String.class);

            comments.add(new Comment(registerDate, writer, comment));
        }
        return comments;
    }
}
