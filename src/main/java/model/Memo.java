package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Memo {
    private int memoId;
    private String content;
    private User writer;
    private LocalDateTime createdTime;

    public Memo(String content, User writer) {
        this.content = content;
        this.writer = writer;
        this.createdTime = LocalDateTime.now();
    }

    public Memo(String content, LocalDateTime createdTime, User writer) {
        this.content = content;
        this.createdTime = createdTime;
        this.writer = writer;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public String getContent() {
        return content;
    }

    public User getWriter() {
        return writer;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return "Memo [memoId=" + memoId + ", content=" + content + ", writer=" + writer + "]";
    }
}
