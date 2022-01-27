package webserver.exception;

import webserver.http.HttpStatus;

public class WebServerException extends RuntimeException {

    private final HttpStatus httpStatus;

    public WebServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public WebServerException(String errorMessage) {
        super(errorMessage);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public WebServerException(HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.httpStatus = httpStatus;
    }

    public WebServerException(HttpStatus httpStatus, String errorMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
