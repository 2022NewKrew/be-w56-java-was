package model;

import java.time.LocalDateTime;

public class Session {

    private final int sessionId;
    private final String userId;
    private final LocalDateTime expire;

    public Session(int sessionId, String userId, LocalDateTime expire) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expire = expire;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isExpired() {
        return expire.isBefore(LocalDateTime.now());
    }
}
