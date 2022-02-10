package http;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    CSS("text/css"),
    JS("text/javascript"),
    HTML("text/html"),
    DEFAULT("application/octet-stream");

    private static final Map<String, ContentType> reverseMap;

    static {
        reverseMap = new HashMap<>();
        reverseMap.put("css", ContentType.CSS);
        reverseMap.put("javascript", ContentType.JS);
        reverseMap.put("html", ContentType.HTML);
    }

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String value() { return value; }

    public static ContentType getTypeFromUrl(String url) {
        if (url == null) {
            return ContentType.DEFAULT;
        }
        String[] tokens = url.split("\\.");
        String extension = tokens[tokens.length - 1];

        return ContentType.get(extension);
    }

    public static ContentType get(String key) {
        if (!reverseMap.containsKey(key)) {
            return ContentType.DEFAULT;
        }

        return reverseMap.get(key);
    }
}
