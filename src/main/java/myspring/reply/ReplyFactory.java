package myspring.reply;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class ReplyFactory {

    public static Reply createReply(long seq, long userSeq, long articleSeq) {
        return new Reply(seq, userSeq, articleSeq, "", "", null, false);
    }

    public static Reply createReply(long userSeq, long articleSeq, String writer, String content) {
        return new Reply(0, userSeq, articleSeq, writer, content, null, false);
    }

    public static Reply createReply(long seq, long userSeq, long articleSeq, @NotNull String writer, @NotNull String content, LocalDateTime time, boolean deleted) {
        return new Reply(seq, userSeq, articleSeq, writer, content, time, deleted);
    }

}
