package domain.session;

import java.time.Duration;

public class Session<T> {

    private final SessionId id;
    private final String attributeName;
    private final T attributeValue;
    private final Duration duration;

    public Session(SessionId id, String attributeName, T attributeValue, Duration duration) {
        this.id = id;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.duration = duration;
    }

    public String getId() {
        return id.getValue();
    }

    public T getAttributeValue() {
        return attributeValue;
    }

    public boolean isExpired() {
        return duration.isZero();
    }
}
