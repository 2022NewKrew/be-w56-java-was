package model;

import java.time.LocalDate;

public class Memo {

    private Long id;
    private String author;
    private final Long authorId;
    private final String content;
    private final LocalDate createdAt;

    public Memo(Long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDate.now();
    }

    public Memo(Long id, String author, Long authorId, String content, LocalDate createdAt) {
        this.id = id;
        this.author = author;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
