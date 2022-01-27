package webserver.exception.request;

import webserver.exception.msg.RequestErrorMsg;

public class BadRequestMethodException extends BadRequestException {
    public BadRequestMethodException(RequestErrorMsg msg) {
        super(msg);
    }
}
