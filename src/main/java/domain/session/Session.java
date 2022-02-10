package domain.session;

import java.time.LocalDateTime;

public class Session {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiryTime;

    public Session(Long id, LocalDateTime createdAt, LocalDateTime expiryTime) {
        this.id = id;
        this.createdAt = createdAt;
        this.expiryTime = expiryTime;
    }

    public static Session create(Long id, int expiryHour) {
        return new Session(id, LocalDateTime.now(), LocalDateTime.now().plusHours(expiryHour));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired(LocalDateTime localDateTime) {
        return expiryTime.isBefore(localDateTime);
    }
}
