package model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.bson.types.ObjectId;

public class Article extends BaseTime {

    private final ObjectId id;
    private final String author;
    private final String content;

    public Article(ObjectId id, String author, String content,
            LocalDateTime createTime, LocalDateTime modifiedTime) {
        super(createTime, modifiedTime);

        checkId(id);
        checkAuthor(author);
        checkContent(content);

        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Article(ObjectId id, String author, String content, LocalDateTime time) {
        this(id, author, content, time, time);
    }

    public Article(ObjectId id, String author, String content) {
        this(id, author, content, LocalDateTime.now(ZoneOffset.UTC));
    }

    private void checkId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("illegal id");
        }
    }

    private void checkAuthor(String author) {
        if (author == null) {
            throw new IllegalArgumentException("illegal author");
        }
    }

    private void checkContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("illegal content");
        }
    }

    public ObjectId getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
