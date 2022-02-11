package exception;

public class NoSuchPathException extends Exception{
    public NoSuchPathException() {

    }

    public NoSuchPathException(String message) {
        super(message);
    }

    public NoSuchPathException(Throwable cause) {
        super(cause);
    }

    public NoSuchPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
