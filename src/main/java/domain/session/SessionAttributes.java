package domain.session;

public class SessionAttributes {

    private final Long sessionId;
    private final String attributeName;
    private final Object attributeValue;

    public SessionAttributes(Long sessionId, String attributeName, Object attributeValue) {
        this.sessionId = sessionId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }
}
