package domain.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Memo{
    private Long id;
    private LocalDateTime createdAt;
    private String author;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Memo of(Map<String, String> params) {
        Memo memo = new Memo();
        memo.setAuthor(params.get("author"));
        memo.setContent(params.get("content"));
        return memo;
    }
}
