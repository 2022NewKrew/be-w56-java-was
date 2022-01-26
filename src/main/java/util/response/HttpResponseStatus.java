package util.response;

import lombok.Getter;

@Getter
public enum HttpResponseStatus {
    SUCCESS(200, "OK"), REDIRECT(302, "Found"),
    NOT_FOUND(404, "Not Found"), INTERNAL_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String text;

    HttpResponseStatus(int statusCode, String text) {
        this.statusCode = statusCode;
        this.text = text;
    }
}
