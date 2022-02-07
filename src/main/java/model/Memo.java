package model;

import java.time.LocalDateTime;

public class Memo {

    private String name;
    private String content;
    private LocalDateTime createdAt;

    public Memo(String name, String content) {
        this.name = name;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
