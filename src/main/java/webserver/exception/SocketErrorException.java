package webserver.exception;

public class SocketErrorException extends RuntimeException {

    public SocketErrorException() {
        super();
    }

    public SocketErrorException(String message) {
        super(message);
    }

    public SocketErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
