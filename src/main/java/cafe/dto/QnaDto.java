package cafe.dto;

import cafe.model.Qna;

import java.time.LocalDateTime;

public class QnaDto {
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime createdAt;

    public QnaDto(String title, String contents, String writer, LocalDateTime createdAt) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public static QnaDto of(Qna qna) {
        return new QnaDto(qna.getTitle(), qna.getContents(), qna.getWriter(), qna.getCreatedAt());
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWriter() {
        return writer;
    }
}
