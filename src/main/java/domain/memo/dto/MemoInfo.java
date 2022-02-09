package domain.memo.dto;

import domain.memo.model.Memo;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class MemoInfo {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final String createdAt;
    private final String author;
    private final String content;

    public static MemoInfo from(Memo memo) {
        return new MemoInfo(memo.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
            memo.getAuthor(), memo.getContent());
    }

    private MemoInfo(String createdAt, String author, String content) {
        this.createdAt = createdAt;
        this.author = author;
        this.content = content;
    }
}
