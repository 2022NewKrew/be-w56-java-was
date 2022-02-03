package exception;

public class InValidPathException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 경로입니다. path = ";

    public InValidPathException(String path) {
        super(MESSAGE + path);
    }
}
