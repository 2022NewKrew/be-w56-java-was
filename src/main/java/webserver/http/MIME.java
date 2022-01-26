package webserver.http;

import java.util.Arrays;

public enum MIME {

    HTML("text/html", ".html", true),
    CSS("text/css", ".css", true),
    JPEG("image/jpeg", ".jpg", true),
    PNG("image/png", ".png", true),
    JS("application/javascript", ".js", true),
    ICO("image/x-icon", ".ico", true),
    TTF("application/x-font-ttf", ".ttf", true),
    WOFF("application/x-font-woff", ".woff", true),
    NONE("*/*", "", false);

    private final String contentType;
    private final String extension;
    private final boolean staticResource;

    MIME(String contentType, String extension, boolean staticResource) {
        this.contentType = contentType;
        this.extension = extension;
        this.staticResource = staticResource;
    }

    public static MIME from(String path) {
        if (path == null) {
            return NONE;
        }
        return Arrays.stream(values())
                .filter(m -> path.endsWith(m.extension))
                .findFirst()
                .orElse(NONE);
    }

    public boolean isStaticResource() {
        return staticResource;
    }

    public String getContentType() {
        return contentType;
    }
}
