package exception;

public class ContextInitializationFailedException extends RuntimeException {

    public ContextInitializationFailedException(String msg) {
        super(msg);
    }
}
