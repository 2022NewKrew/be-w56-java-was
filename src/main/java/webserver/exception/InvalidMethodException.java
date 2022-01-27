package webserver.exception;

import webserver.http.HttpStatus;

public class InvalidMethodException extends WebServerException {

    public InvalidMethodException() {
        super(HttpStatus.METHOD_NOT_ALLOWED);
    }

    public InvalidMethodException(String errorMessage) {
        super(HttpStatus.METHOD_NOT_ALLOWED, errorMessage);
    }
}
