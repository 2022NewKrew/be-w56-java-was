package app.model;

import java.time.LocalDateTime;

public class Post {
    private final LocalDateTime createdDate;
    private final long id;
    private final String writer;
    private final String content;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }


    public Post(LocalDateTime createdDate, long id, String writer, String content) {
        this.createdDate = createdDate;
        this.id = id;
        this.writer = writer;
        this.content = content;
    }

    public Post(String writer, String content){
        this.createdDate = null;
        this.id = -1;
        this.writer = writer;
        this.content = content;
    }
}
