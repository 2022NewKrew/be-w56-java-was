package exceptions;

public class InvalidHttpVersionException extends RuntimeException {

    public InvalidHttpVersionException(String message) {
        super(message);
    }
}
