package bin.jayden.exception;

public class CannotInjectDependencyException extends RuntimeException {
    public CannotInjectDependencyException(String message) {
        super(message);
    }
}
