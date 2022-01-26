package http.header;

public enum HttpHeaderNames {
    ACCEPT("accept"),
    CONTENT_LENGTH("content-length");

    final String name;

    HttpHeaderNames(String name) {
        this.name = name;
    }
}
