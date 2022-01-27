package webserver.exception;

import webserver.model.HttpStatus;

public class AuthenticationFailureException extends BaseException {

    public AuthenticationFailureException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
