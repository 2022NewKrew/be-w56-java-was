package http;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MediaType {

    APPLICATION_JAVASCRIPT("application/javascript", "js"),
    APPLICATION_TTF("application/x-font-ttf", "ttf"),
    APPLICATION_WOFF("application/x-font-woff", "woff"),
    APPLICATION_WOFF2("application/x-font-woff2", "woff2"),

    IMAGE_GIF("image/gif", "gif"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_ICON("image/x-icon", "ico"),

    TEXT_CSS("text/css", "css"),
    TEXT_HTML("text/html", "html");

    private static final Map<String, MediaType> mappings =
        Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(MediaType::getExtension, Function.identity())));
    private final String value;
    private final String extension;

    MediaType(String value, String extension) {
        this.value = value;
        this.extension = extension;
    }

    public static String fromExtension(String extension) {
        return mappings.get(extension).getValue();
    }

    public String getValue() {
        return value;
    }

    public String getExtension() {
        return extension;
    }
}
