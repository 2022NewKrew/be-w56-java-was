package webserver.exception;

import webserver.http.HttpStatus;

import java.io.DataOutputStream;

public class BadRequestException extends WebServerException {

    public BadRequestException(DataOutputStream dos) {
        super(dos, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(DataOutputStream dos, String errorMessage) {
        super(dos, HttpStatus.BAD_REQUEST, errorMessage);
    }
}
