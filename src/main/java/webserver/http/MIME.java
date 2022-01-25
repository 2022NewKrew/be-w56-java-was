package webserver.http;

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
    ;
    private final String contentType;
    private final String extensions;

    MIME(String contentType, String extensions) {
        this.contentType = contentType;
        this.extensions = extensions;
    }

    public static String parse(String path) {
        MIME mime = Arrays.stream(values())
                .filter(m -> path.endsWith(m.extensions))
                .findFirst().orElse(HTML);
        return mime.contentType;
    }
}
