package domain.memo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoCreate {

    private final String author;
    private final String content;

    @Builder
    public MemoCreate(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
