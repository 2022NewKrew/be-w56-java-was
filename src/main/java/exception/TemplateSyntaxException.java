package exception;

import http.HttpStatus;

public class TemplateSyntaxException extends CustomException {

    public TemplateSyntaxException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
