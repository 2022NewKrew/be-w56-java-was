package http.common;

import java.util.Arrays;

public enum HttpVersion {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public static HttpVersion fromString(String version) {
        return Arrays.stream(HttpVersion.values())
                .filter((httpVersion -> httpVersion.version.equals(version)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
