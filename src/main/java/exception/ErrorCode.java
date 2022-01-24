package exception;

import util.HttpStatus;

public enum ErrorCode {
    UNSUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "Unsupported method"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! An unexpected error seems to have occurred.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
