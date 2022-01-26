package webserver.exception.request;

import webserver.exception.msg.RequestErrorMsg;

public class BadRequestLineException extends BadRequestException {
    public BadRequestLineException(RequestErrorMsg msg) {
        super(msg);
    }
}
