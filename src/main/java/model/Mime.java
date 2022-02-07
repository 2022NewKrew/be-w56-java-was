package model;

import java.util.HashMap;
import java.util.Map;

public enum Mime {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/js"),
    JPEG("jpeg", "image/jpeg"),
    TTF("ttf", "application/font-ttf"),
    WOFF("woff", "application/font-woff"),
    ICON("ico", "image/x-icon"),
    DEFAULT("", "application/octet-stream");

    private final static Map<String, String> mimeMap = new HashMap<>();

    static {
        for (Mime mime : Mime.values()) {
            mimeMap.put(mime.getExtension(), mime.getType());
        }
    }

    public static String getMime(String key) {
        return mimeMap.getOrDefault(key, DEFAULT.getType());
    }

    private final String extension;
    private final String type;

    Mime(String extension, String type) {
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
