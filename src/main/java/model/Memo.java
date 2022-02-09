package model;

import dto.memo.MemoCreateDto;

import java.time.LocalDate;

public class Memo {
    private final String content;
    private final LocalDate createdAt;
    private final User writer;

    public Memo(String content, LocalDate createdAt, User writer) {
        this.content = content;
        this.createdAt = createdAt;
        this.writer = writer;
    }

    public static Memo of(MemoCreateDto memoCreateDto, User writer) {
        return new Memo(memoCreateDto.getContent(), LocalDate.now(), writer);
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public User getWriter() {
        return writer;
    }
}
