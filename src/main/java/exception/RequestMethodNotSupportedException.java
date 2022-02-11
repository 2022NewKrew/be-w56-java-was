package exception;

public class RequestMethodNotSupportedException extends Exception {
    public RequestMethodNotSupportedException() {

    }

    public RequestMethodNotSupportedException(String message) {
        super(message);
    }

    public RequestMethodNotSupportedException(Throwable cause) {
        super(cause);
    }

    public RequestMethodNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
