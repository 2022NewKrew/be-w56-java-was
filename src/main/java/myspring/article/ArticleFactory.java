package myspring.article;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class ArticleFactory {

    public static Article createArticle(long seq, long userSeq) {
        return new Article(seq, userSeq, "", "", "", null, 0, false);
    }

    public static Article createArticle(@NotNull String writer, @NotNull String title, @NotNull String content) {
        return new Article(0, 0, writer, title, content, null, 0, false);
    }

    public static Article createArticle(long userSeq, @NotNull String writer, @NotNull String title, @NotNull String content) {
        return new Article(0, userSeq, writer, title, content, null, 0, false);
    }

    public static Article createArticle(long seq, long userSeq, @NotNull String writer, @NotNull String title, @NotNull String content) {
        return new Article(seq, userSeq, writer, title, content, null, 0, false);
    }

    public static Article createArticle(long seq, long userSeq, @NotNull String writer, @NotNull String title, @NotNull String content, LocalDateTime time, int replyCount, boolean deleted) {
        return new Article(seq, userSeq, writer, title, content, time, replyCount, deleted);
    }

}
