package webserver.exception;

import webserver.http.HttpStatus;

import java.io.DataOutputStream;

public class WebServerException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final DataOutputStream dos;

    public WebServerException(DataOutputStream dos) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorMessage = httpStatus.toString();
        this.dos = dos;
    }

    public WebServerException(DataOutputStream dos, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = httpStatus.toString();
        this.dos = dos;
    }

    public WebServerException(DataOutputStream dos, HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.dos = dos;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public DataOutputStream getDos() {
        return dos;
    }
}
