package exceptions;

public class InvalidRequestFormatException extends RuntimeException{
    public InvalidRequestFormatException(String message) {
        super(message);
    }
}
