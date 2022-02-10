package model;

import java.time.LocalDate;

public class Memo {

    private long id;
    private final String author;
    private final String content;
    private final LocalDate createAt;

    public Memo(String author, String content, LocalDate createAt) {
        this.author = author;
        this.content = content;
        this.createAt = createAt;
    }

    public Memo(long id, String author, String content, LocalDate createAt) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createAt = createAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }
}
