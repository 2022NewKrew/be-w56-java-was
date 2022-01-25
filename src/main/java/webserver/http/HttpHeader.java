package webserver.http;

public enum HttpHeader {
    DATE("Date"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    ;

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
