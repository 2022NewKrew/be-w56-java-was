package http;

public enum HttpVersion {
    HTTP_1_1 ("HTTP/1.1"),
    HTTP_2 ("HTTP/2");

    private final String string;
    HttpVersion(String string) {
        this.string = string;
    }
    public String getString() {
        return string;
    }
    public static HttpVersion fromString(String string) {
        return HttpVersion.valueOf(string.replaceAll("[/.]", "_"));
    }
}
