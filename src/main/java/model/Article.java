package model;

import org.bson.types.ObjectId;

public class Article {
    private final ObjectId id;
    private final String title;
    private final String author;
    private final String content;

    public Article(ObjectId id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
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
