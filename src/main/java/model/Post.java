package model;

import java.time.LocalDate;

public class Post {
    private final LocalDate createdAt;
    private final String author;
    private final String contents;

    public Post(LocalDate createdAt, String author, String contents) {
        this.createdAt = createdAt;
        this.author = author;
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "Post{" +
                "createdAt=" + createdAt +
                ", author='" + author + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
