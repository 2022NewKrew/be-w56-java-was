package http.response;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ContentType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/js"),
    TTF("ttf", "application/font-ttf"),
    WOFF("woff", "application/font-woff"),
    DEFAULT("", "application/octet-stream");

    private static Map<String, String> typeMap = new ConcurrentHashMap<>();

    static {
        for (ContentType contentType : ContentType.values()) {
            typeMap.put(contentType.getExtension(), contentType.getType());
        }
    }

    public static String getContentType(String key) {
        return typeMap.getOrDefault(key, DEFAULT.getType());
    }

    private final String extension;
    private final String type;

    ContentType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }
}
