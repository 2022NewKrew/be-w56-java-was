package exception;

public class IllegalContentTypeException extends RuntimeException{
    public IllegalContentTypeException(String message) {
        super(message);
    }
}
