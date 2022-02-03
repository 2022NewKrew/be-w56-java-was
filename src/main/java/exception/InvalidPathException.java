package exception;

public class InvalidPathException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 경로입니다. path = ";

    public InvalidPathException(String path) {
        super(MESSAGE + path);
    }
}
