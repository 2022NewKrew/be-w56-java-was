package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum MimeType {
    HTML("html", HttpHeaderValue.TEXT_HTML),
    CSS("css", HttpHeaderValue.TEXT_CSS),
    JAVASCRIPT("js", HttpHeaderValue.APPLICATION_JS),
    JSON("json", HttpHeaderValue.APPLICATION_JSON),
    ;

    private static final Map<String, HttpHeaderValue> mimeMap;

    static {
        mimeMap = new HashMap<>();
        Arrays.stream(values()).forEach(value -> mimeMap.put(value.extension, value.headerValue));
    }

    private final String extension;
    private final HttpHeaderValue headerValue;

    MimeType(String extension, HttpHeaderValue headerValue) {
        this.extension = extension;
        this.headerValue = headerValue;
    }

    public static HttpHeaderValue getExtensionHeaderValue(String extension) {
        HttpHeaderValue httpHeaderValue = mimeMap.get(extension);
        if (httpHeaderValue == null) {
            throw new IllegalArgumentException("Content type을 찾을 수 없습니다.");
        }
        return httpHeaderValue;
    }
}
