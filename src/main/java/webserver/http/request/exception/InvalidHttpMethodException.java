package webserver.http.request.exception;

public class InvalidHttpMethodException extends RuntimeException{
    public InvalidHttpMethodException(String message) {
        super(message);
    }
}
