package domain.memo.model;

import java.time.LocalDate;
import lombok.Builder;

public class Memo {

    private final LocalDate createdAt;
    private final String author;
    private final String content;

    @Builder
    public Memo(LocalDate createdAt, String author, String content) {
        this.createdAt = createdAt;
        this.author = author;
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
