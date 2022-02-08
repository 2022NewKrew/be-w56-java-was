package model;

import java.time.LocalDate;

public class Memo {

    private final long id;
    private final String userId;
    private final String memo;
    private final LocalDate createdAt;

    public Memo(String userId, String memo) {
        this(0L, userId, memo);
    }

    public Memo(long id, String userId, String memo) {
        this(id, userId, memo, LocalDate.now());
    }

    public Memo(long id, String userId, String memo, LocalDate createdAt) {
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
