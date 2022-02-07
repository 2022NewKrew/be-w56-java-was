package myspring.article;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Article {

    private long seq;

    private long userSeq;

    @NotNull
    private String writer;
    @NotNull
    private String title;
    @NotNull
    private String content;

    private LocalDateTime time;

    private int replyCount;

    private boolean deleted;

    public Article(long seq, long userSeq, @NotNull String writer, @NotNull String title, @NotNull String content, LocalDateTime time, int replyCount, boolean deleted) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.time = time;
        this.replyCount = replyCount;
        this.deleted = deleted;
    }

    public long getSeq() {
        return seq;
    }

    public long getUserSeq() {
        return userSeq;
    }

    public @NotNull String getWriter() {
        return writer;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
