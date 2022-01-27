package webserver.exception;

import webserver.http.HttpStatus;

public class UserUnauthorizedException extends WebServerException {

    public UserUnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public UserUnauthorizedException(String errorMessage) {
        super(HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
