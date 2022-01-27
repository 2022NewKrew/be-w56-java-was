package was.meta;

import java.util.EnumSet;

public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302, "Found"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVICE_ERROR(500, "Internal Server Error");

    private final int code;
    private final String text;

    private HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getValue() {
        return String.valueOf(code) + " " + text;
    }

    public static HttpStatus of(String status) {
        return EnumSet.allOf(HttpStatus.class).stream()
                .filter(httpStatus -> httpStatus.matchCodeAndText(status))
                .findAny()
                .orElse(INTERNAL_SERVICE_ERROR);
    }

    private boolean matchCodeAndText(String status) {
        return this.getValue().equals(status);
    }
}
