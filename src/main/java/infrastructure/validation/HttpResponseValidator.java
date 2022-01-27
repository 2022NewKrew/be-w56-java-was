package infrastructure.validation;

import infrastructure.model.HttpStatus;

public class HttpResponseValidator {

    private HttpResponseValidator() {
    }

    public static void assertStatusCode(HttpStatus status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("Status가 비어있습니다.");
        }
    }
}
