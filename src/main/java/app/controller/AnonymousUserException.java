package app.controller;

public class AnonymousUserException extends RuntimeException {
    public AnonymousUserException() {
        super();
    }

    public AnonymousUserException(String message) {
        super(message);
    }

    public AnonymousUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
