package exception;

import webserver.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
