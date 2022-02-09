package webserver.http.enums;

import java.util.Arrays;


/**
 * copied from: https://github.dev/kakao-2022/be-w56-java-was/pull/66/commits
 */
public enum MIME {
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

    MIME(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static MIME parse(String path) {
        MIME mime = Arrays.stream(values())
                .filter(m -> path.endsWith(m.extension))
                .findFirst().orElse(HTML);

        return mime;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isExtensionMatch(String path) {
        return path.endsWith(extension);
    }

    public String getExtension() {
        return extension;
    }
}