package http;

import java.util.Arrays;

public enum Mime {
    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JPEG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    JS("application/javascript", ".js"),
    ICO("image/x-icon", ".ico"),
    TTF("application/x-font-ttf", ".ttf"),
    WOFF("application/x-font-woff", ".woff");

    private final String contentType;
    private final String extension;

    Mime(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static Mime getMineByPath(String path) {
        return Arrays.stream(values())
                .filter(mime -> path.endsWith(mime.extension))
                .findFirst()
                .orElse(HTML);
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }
}
