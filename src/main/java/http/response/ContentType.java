package http.response;

import java.util.Arrays;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/js"),
    TTF("ttf", "application/font-ttf"),
    WOFF("woff", "application/font-woff"),
    DEFAULT("", "application/octet-stream");

    private final String extension;
    private final String type;

    ContentType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public static ContentType findTypeByExtension(String extension) {
        return Arrays
                .stream(ContentType.values())
                .filter(contentType -> contentType.getExtension().equals(extension))
                .findFirst()
                .orElse(ContentType.DEFAULT);
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }
}
