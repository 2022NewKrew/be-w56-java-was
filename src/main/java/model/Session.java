package model;

import java.time.LocalDateTime;

public class Session {

    private final int sessionId;
    private final LocalDateTime expire;

    public Session(int sessionId, LocalDateTime expire) {
        this.sessionId = sessionId;
        this.expire = expire;
    }

    public int getSessionId() {
        return sessionId;
    }

    public boolean isExpired() {
        return expire.isBefore(LocalDateTime.now());
    }
}
