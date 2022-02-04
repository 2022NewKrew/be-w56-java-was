package exception;

public class InvalidHttpMethodException extends Exception{
    public InvalidHttpMethodException(String message) {
        super(message);
    }
}
