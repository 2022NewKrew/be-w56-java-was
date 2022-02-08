package exceptions;

public class BadRequestFormatException extends RuntimeException{
    public BadRequestFormatException(String message) {
        super(message);
    }
}
