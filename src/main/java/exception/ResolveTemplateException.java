package exception;

import webserver.http.HttpStatus;

public class ResolveTemplateException extends BaseException {

    public ResolveTemplateException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResolveTemplateException() {
        this("화면을 랜더링하는 과정에서 문제가 발생하였습니다.");
    }
}
