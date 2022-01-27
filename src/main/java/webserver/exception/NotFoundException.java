package webserver.exception;

import webserver.model.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
