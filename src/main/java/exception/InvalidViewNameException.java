package exception;

import http.HttpStatus;

public class InvalidViewNameException extends CustomException {

    private static final String MESSAGE = "유효하지 않은 viewName 입니다. viewName = ";

    public InvalidViewNameException(String viewName) {
        super(MESSAGE + viewName, HttpStatus.BAD_REQUEST);
    }
}
