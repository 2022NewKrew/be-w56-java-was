package dto;

import java.time.LocalDateTime;

public class ArticleDto {

    private final LocalDateTime createTime;
    private final String author;
    private final String content;

    public ArticleDto(LocalDateTime createTime, String author, String content) {
        this.createTime = createTime;
        this.author = author;
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
