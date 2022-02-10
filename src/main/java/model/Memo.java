package model;

import java.time.LocalDate;

public class Memo {
    private final LocalDate createdAt;
    private final String writer;
    private final String title;
    private final String content;

    public Memo(LocalDate createdAt, String writer, String title, String content) {
        this.createdAt = createdAt;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "createdAt=" + createdAt +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
