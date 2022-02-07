package myspring.reply;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Reply {

    private long seq;

    private long userSeq;

    private long articleSeq;

    @NotNull
    private String writer;
    @NotNull
    private String content;

    private LocalDateTime time;

    private boolean deleted;

    public Reply(long seq, long userSeq, long articleSeq, @NotNull String writer, @NotNull String content, LocalDateTime time, boolean deleted) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.articleSeq = articleSeq;
        this.writer = writer;
        this.content = content;
        this.time = time;
        this.deleted = deleted;
    }

    public long getSeq() {
        return seq;
    }

    public long getUserSeq() {
        return userSeq;
    }

    public long getArticleSeq() {
        return articleSeq;
    }

    public @NotNull String getWriter() {
        return writer;
    }

    public @NotNull String getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean isDeleted() {
        return deleted;
    }

}
