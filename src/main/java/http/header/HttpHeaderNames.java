package http.header;

public enum HttpHeaderNames {
    ACCEPT("accept"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    ;

    final String name;

    HttpHeaderNames(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString().toLowerCase();
    }
}
