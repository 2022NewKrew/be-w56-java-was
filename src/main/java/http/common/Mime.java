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
    PLAIN_TEXT(".*", "text/plain"),
    X_URL_FORM_ENCODED(".*", "application/x-url-form-encoded");

    private final String extension;
    private final String contentType;

    Mime(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static Mime of(String target) {
        return Arrays.stream(Mime.values())
                .filter((mime -> target.endsWith(mime.extension)))
                .findAny()
                .orElse(Mime.PLAIN_TEXT);
    }

    public String getContentType() {
        return contentType;
    }
}
