package webserver.exception;

import webserver.model.HttpStatus;

public class InvalidInputException extends BaseException {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
