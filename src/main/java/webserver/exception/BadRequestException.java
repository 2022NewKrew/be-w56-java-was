package webserver.exception;

import webserver.http.HttpStatus;

public class BadRequestException extends WebServerException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
