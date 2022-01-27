package webserver.exception;

public class InvalidRequestMethodException extends RuntimeException {
    public InvalidRequestMethodException() {
    }

    public InvalidRequestMethodException(String message) {
        super(message);
    }
}
