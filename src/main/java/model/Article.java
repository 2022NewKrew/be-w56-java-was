package model;

import java.time.LocalDateTime;
import org.bson.types.ObjectId;

public class Article extends BaseTime {

    private final ObjectId id;
    private final String title;
    private final String author;
    private final String content;

    public Article(ObjectId id, String title, String author, String content,
            LocalDateTime createTime, LocalDateTime modifiedTime) {
        super(createTime, modifiedTime);
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public Article(ObjectId id, String title, String author, String content) {
        this(id, title, author, content, LocalDateTime.now(), LocalDateTime.now());
    }

    public ObjectId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
