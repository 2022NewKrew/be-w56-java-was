package framework.exception;

public class InvalidRequestLineException extends RuntimeException{
    public InvalidRequestLineException() {
        super("Invalid Request Line.");
    }
}
