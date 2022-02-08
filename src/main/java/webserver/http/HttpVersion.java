package webserver.http;

public enum HttpVersion {
    HTTP_1("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2("HTTP/2.0");

    private final String text;

    HttpVersion(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static HttpVersion of(String version) {
        for (HttpVersion httpVersion : values()) {
            if (httpVersion.text.equals(version)) {
                return httpVersion;
            }
        }
        return HTTP_1;
    }
}
