package webserver.exception;

import webserver.http.HttpStatus;

import java.io.DataOutputStream;

public class ResourceNotFoundException extends WebServerException {

    public ResourceNotFoundException(DataOutputStream dos) {
        super(dos, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(DataOutputStream dos, String errorMessage) {
        super(dos, HttpStatus.NOT_FOUND, errorMessage);
    }
}
