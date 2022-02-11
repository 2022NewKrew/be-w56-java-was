package exception;

import webserver.response.StatusCode;

public class TemplateProcessingException extends CustomException {

    public TemplateProcessingException(String msg) {
        super(StatusCode.INTERNAL_SERVER_ERROR, msg);
    }
}
