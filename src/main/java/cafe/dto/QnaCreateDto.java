package cafe.dto;

import cafe.model.Qna;

public class QnaCreateDto {
    private String writer;
    private String title;
    private String contents;

    public QnaCreateDto() {
    }

    public Qna toEntity() {
        return new Qna(writer, title, contents);
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
}
