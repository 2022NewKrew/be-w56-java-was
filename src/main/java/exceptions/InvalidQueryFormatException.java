package exceptions;

public class InvalidQueryFormatException extends RuntimeException{
    public InvalidQueryFormatException(String message) {
        super(message);
    }
}
