package http;

import exception.ContentTypeNotFoundException;
import java.util.Arrays;

public enum ContentType {

    TEXT_HTML("text/html", ".html"),
    TEXT_CSS("text/css", ".css"),

    IMAGE_X_ICON("image/x-icon", ".ico"),
    IMAGE_SVG_XML("image/svg+xml", ".svg"),

    APPLICATION_JSON("application/json", ".js"),
    APPLICATION_X_FONT_TTF("application/x-font-ttf", ".ttf"),
    APPLICATION_X_FONT_WOFF("	application/x-font-woff", ".woff");

    private final String type;
    private final String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public static ContentType match(String path) {
        return Arrays.stream(ContentType.values())
                .filter(value -> path.contains(value.extension))
                .findAny()
                .orElseThrow(ContentTypeNotFoundException::new);
    }
}
