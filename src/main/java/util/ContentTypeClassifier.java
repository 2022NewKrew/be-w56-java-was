package util;

import java.util.Arrays;
import java.util.List;

public enum ContentTypeClassifier {
    JS(".js", "text/javascript"),
    CSS(".css", "text/css"),
    DEFAULT("", "text/html");

    private final String extension;
    private final String contentType;

    ContentTypeClassifier(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static String getContentType(String uri) {
        return Arrays.stream(ContentTypeClassifier.values())
                                                    .filter(c -> uri.endsWith(c.extension))
                                                    .findAny()
                                                    .orElse(DEFAULT)
                                                    .contentType;
    }
}
