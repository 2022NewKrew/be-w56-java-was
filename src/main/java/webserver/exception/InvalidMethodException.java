package webserver.exception;

import webserver.http.HttpStatus;

import java.io.DataOutputStream;

public class InvalidMethodException extends WebServerException {

    public InvalidMethodException(DataOutputStream dos) {
        super(dos, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public InvalidMethodException(DataOutputStream dos, String errorMessage) {
        super(dos, HttpStatus.METHOD_NOT_ALLOWED, errorMessage);
    }
}
