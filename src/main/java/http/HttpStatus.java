package http;

import static util.Constant.BLANK;
import static util.Constant.NEW_LINE;

public enum HttpStatus {
    OK(200),
    FOUND(302),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501);

    private final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode + BLANK + name() + NEW_LINE;
    }
}
