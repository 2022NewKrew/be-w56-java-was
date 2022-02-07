package exception;

import webserver.http.HttpStatus;

public class InvalidInputException extends BaseException {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
