package exception;

import http.HttpStatus;

public class InvalidPathException extends CustomException {

    private static final String MESSAGE = "유효하지 않은 경로입니다. path = ";

    public InvalidPathException(String path) {
        super(MESSAGE + path, HttpStatus.BAD_REQUEST);
    }
}
