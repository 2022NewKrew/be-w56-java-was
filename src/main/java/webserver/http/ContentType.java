package webserver.http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ContentType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JS("application/javascript", "js"),
    JSON("application/json", "json"),
    ICO("image/x-icon", "ico");

    private final String type;
    private final String extension;

    private static final Map<String, ContentType> descriptions = Collections
            .unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ContentType::getExtension, Function.identity())));

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public static ContentType matching(String extension) {
        return Optional.ofNullable(descriptions.get(extension)).orElse(HTML);
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }
}
