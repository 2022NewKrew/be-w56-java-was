package cafe.model;

import java.time.LocalDateTime;

public class Qna {
    private String id;
    private String writer;
    private String title;
    private String contents;
    private Boolean deleted;
    private LocalDateTime created_at;

    public Qna(String writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
        this.created_at = LocalDateTime.now();
    }

    public Qna(String writer, String title, String contents, LocalDateTime created_at) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
        this.created_at = created_at;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
