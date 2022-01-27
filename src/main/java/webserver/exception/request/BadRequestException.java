package webserver.exception.request;

import webserver.exception.msg.RequestErrorMsg;

public class BadRequestException extends RuntimeException {

    protected RequestErrorMsg msg;

    public BadRequestException(RequestErrorMsg msg) {
        this.msg = msg;
    }

    public RequestErrorMsg getMsg() {
        return msg;
    }
}
