package webserver.exception;

import webserver.http.HttpStatus;

public class ResourceNotFoundException extends WebServerException {

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
