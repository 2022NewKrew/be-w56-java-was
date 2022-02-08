package http.header;

public enum HttpHeaderName {
    ACCEPT("accept"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    ;

    final String value;

    HttpHeaderName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value.toLowerCase();
    }
}
