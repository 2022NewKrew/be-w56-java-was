package exception;

import webserver.http.HttpStatus;

public class AuthenticationFailureException extends BaseException {

    public AuthenticationFailureException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
