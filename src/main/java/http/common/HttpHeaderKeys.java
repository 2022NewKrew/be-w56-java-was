package http.common;

public enum HttpHeaderKeys {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    SET_COOKIE("Set-Cookie"),
    LOCATION("Location");

    private final String key;

    HttpHeaderKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
