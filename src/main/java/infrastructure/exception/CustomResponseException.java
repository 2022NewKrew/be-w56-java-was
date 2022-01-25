package infrastructure.exception;

import infrastructure.model.HttpStatus;

public class CustomResponseException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CustomResponseException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
