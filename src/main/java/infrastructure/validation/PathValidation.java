package infrastructure.validation;

import infrastructure.model.ContentType;

public class PathValidation {

    private PathValidation() {
    }

    public static void assertContentType(ContentType contentType) throws IllegalArgumentException {
        if (contentType == null) {
            throw new IllegalArgumentException("Content Type이 비어있습니다.");
        }
    }
}
