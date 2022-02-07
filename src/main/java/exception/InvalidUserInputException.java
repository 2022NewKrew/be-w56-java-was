package exception;

import http.HttpStatus;

public class InvalidUserInputException extends CustomException {

    public InvalidUserInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
