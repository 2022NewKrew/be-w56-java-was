package model;

import util.Checker;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class Memo {
    public static final int BODY_MIN = 1;
    public static final int BODY_MAX = 255;

    private static final DateTimeFormatter DT_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withLocale(Locale.KOREA)
            .withZone(ZoneId.of("Asia/Seoul"));

    private final String createdAt;
    private final String userId;
    private final String body;

    public Memo(
            final String userId,
            final String body
    ) throws IllegalArgumentException
    {
        validate(userId, body);
        this.userId = userId;
        this.body = body.trim();
        this.createdAt = DT_FORMATTER.format(Instant.now());
    }

    private void validate(
            final String userId,
            final String body
    )
    {
        Checker.checkString("userId", userId, User.ID_REGEX, User.ID_MIN, User.ID_MAX);
        Checker.checkString("body", body, BODY_MIN, BODY_MAX);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memo memo = (Memo) o;
        return createdAt.equals(memo.createdAt) && userId.equals(memo.userId) && body.equals(memo.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, userId, body);
    }
}
