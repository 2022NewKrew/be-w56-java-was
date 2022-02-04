package exception;

import http.HttpStatus;

public class InvalidRequestLineException extends CustomException {

    private static final String MESSAGE = "유효하지 않은 request line 입니다. request line = ";

    public InvalidRequestLineException(String path) {
        super(MESSAGE + path, HttpStatus.BAD_REQUEST);
    }
}
