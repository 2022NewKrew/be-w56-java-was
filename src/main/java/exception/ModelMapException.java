package exception;

import http.HttpStatus;

public class ModelMapException extends CustomException {

    public ModelMapException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
