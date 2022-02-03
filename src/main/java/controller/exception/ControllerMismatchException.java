package controller.exception;

public class ControllerMismatchException extends RuntimeException {

    public ControllerMismatchException(String message) {
        super(message);
    }
}
