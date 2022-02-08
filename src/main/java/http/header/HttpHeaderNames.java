package http.header;

public enum HttpHeaderNames {
    ACCEPT("accept"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    ;

    final String value;

    HttpHeaderNames(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toLowerCase();
    }
}
