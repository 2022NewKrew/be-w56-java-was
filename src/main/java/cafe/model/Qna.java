package cafe.model;

import java.time.LocalDateTime;

public class Qna {
    private String id;
    private String writer;
    private String title;
    private String contents;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public Qna(String writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public Qna(String writer, String title, String contents, LocalDateTime created_at) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
        this.createdAt = created_at;
    }

    public String getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
