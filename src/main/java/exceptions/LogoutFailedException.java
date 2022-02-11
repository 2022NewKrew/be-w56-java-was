package exceptions;

public class LogoutFailedException extends RuntimeException {

    public LogoutFailedException(String message) {
        super(message);
    }
}
