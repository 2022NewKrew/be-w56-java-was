package model;

import java.time.LocalDateTime;

public class Memo {

    private final long id;
    private final String userId;
    private final String memo;
    private final LocalDateTime createdAt;

    public Memo(String userId, String memo) {
        this(0L, userId, memo);
    }

    public Memo(long id, String userId, String memo) {
        this(id, userId, memo, LocalDateTime.now());
    }

    public Memo(long id, String userId, String memo, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.memo = memo;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMemo() {
        return memo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
