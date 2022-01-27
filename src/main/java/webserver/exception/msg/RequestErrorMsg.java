package webserver.exception.msg;

import webserver.response.HttpStatus;

public enum RequestErrorMsg {

    INVALID_HTTP_METHOD(HttpStatus.BAD_REQUEST, "Invalid http method"),
    BAD_REQUEST_LINE(HttpStatus.BAD_REQUEST, "Bad Http Request Line");

    private HttpStatus status;
    private String message;

    RequestErrorMsg(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
