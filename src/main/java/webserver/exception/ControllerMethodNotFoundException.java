package webserver.exception;

public class ControllerMethodNotFoundException extends RuntimeException {
    public ControllerMethodNotFoundException() {
    }

    public ControllerMethodNotFoundException(String message) {
        super(message);
    }
}
