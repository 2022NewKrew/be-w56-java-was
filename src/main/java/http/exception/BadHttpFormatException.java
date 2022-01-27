package http.exception;

public class BadHttpFormatException extends RuntimeException {

    public BadHttpFormatException() {
        super();
    }

    public BadHttpFormatException(String message) {
        super(message);
    }

    public BadHttpFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
