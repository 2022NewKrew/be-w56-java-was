package http.common;

import java.util.Arrays;

public enum Mime {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JAVASCRIPT(".js", "application/javascript"),
    ICON(".ico", "image/vnd.microsoft.icon"),
    PNG(".png", "image/png"),
    SVG(".svg", "image/svg+xml"),
    TTF(".ttf", "font/ttf"),
    WOFF(".woff", "font/woff"),
    WOFF2(".woff2", "font/woff2"),
    EOT(".eot", "application/vnd.ms-fontobject"),
    JSON(".json", "application/json"),
    X_WWW_FORM_URLENCODED(".*", "application/x-www-form-urlencoded"),
    PLAIN_TEXT(".*", "text/plain");

    private final String extension;
    private final String contentType;

    Mime(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static Mime fromFileName(String fileName) {
        return Arrays.stream(Mime.values())
                .filter((mime -> fileName.endsWith(mime.extension)))
                .findAny()
                .orElse(Mime.PLAIN_TEXT);
    }

    public static Mime fromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }

        return Arrays.stream(Mime.values())
                .filter((mime -> mime.contentType.equals(contentType)))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getContentType() {
        return contentType;
    }
}
