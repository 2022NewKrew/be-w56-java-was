package webserver.exception;

import webserver.http.HttpStatus;

public class WebServerException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorMessage;

    public WebServerException() {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorMessage = httpStatus.toString();
    }

    public WebServerException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = httpStatus.toString();
    }

    public WebServerException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
