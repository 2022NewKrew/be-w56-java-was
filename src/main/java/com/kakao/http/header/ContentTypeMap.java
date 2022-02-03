package com.kakao.http.header;

import java.util.Map;

public class ContentTypeMap {
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_CHARSET = "; charset=utf-8";
    private static final Map<String, String> EXTENSION_MAP = Map.ofEntries(
            Map.entry("html", "text/html" + DEFAULT_CHARSET),
            Map.entry("js", "text/html" + DEFAULT_CHARSET),
            Map.entry("css", "text/css" + DEFAULT_CHARSET)
    );

    public static String findByExtension(String extension) {
        String contentType = EXTENSION_MAP.get(extension);
        return contentType == null ? DEFAULT_CONTENT_TYPE : contentType;
    }
}
