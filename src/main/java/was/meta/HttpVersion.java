package was.meta;

import java.util.EnumSet;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1"),
    HTTP_2("HTTP/2");

    private final String value;

    HttpVersion(String msg) {
        this.value = msg;
    }

    public String getValue() {
        return value;
    }

    public static HttpVersion of(String version) {
        return EnumSet.allOf(HttpVersion.class).stream()
                .filter(httpVersion -> httpVersion.matchVersion(version))
                .findAny()
                .orElse(HTTP_1_1);
    }

    private boolean matchVersion(String version) {
        return this.value.equals(version);
    }
}
