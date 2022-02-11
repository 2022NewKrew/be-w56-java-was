package webserver.enums;

import java.util.Arrays;

public enum MIME {
    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JPEG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    JS("application/javascript", ".js"),
    ICO("image/x-icon", ".ico"),
    TTF("application/x-font-ttf", ".ttf"),
    WOFF("application/x-font-woff", ".woff"),
    NULL("", "");

    private final String contentType;
    private final String extension;

    MIME(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static MIME parse(String path) {
        return Arrays.stream(values())
                .filter(m -> path.endsWith(m.extension))
                .findFirst()
                .orElse(NULL);
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isNull() {
        return this.equals(NULL);
    }
}
