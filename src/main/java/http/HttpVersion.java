package http;

public enum HttpVersion {
    HTTP_1_1 ("HTTP/1.1"),
    HTTP_2 ("HTTP/2");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String version() {
        return version;
    }

    public static HttpVersion from(String string) {
        return HttpVersion.valueOf(string.replaceAll("[/.]", "_"));
    }
}
