package webserver.http.response;

import java.util.Optional;

public enum ContentType {
    ANY("*/*"),
    TEXT("text/plain"),
    JSON("application/json"),
    XML("application/xml"),
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    ICO("image/x-icon"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    WOFF("application/font-woff");

    private final String extension;

    ContentType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static ContentType of(String inputExtension) {
        return Optional.of(ContentType.valueOf(inputExtension.toUpperCase())).orElse(ANY);
    }
}
