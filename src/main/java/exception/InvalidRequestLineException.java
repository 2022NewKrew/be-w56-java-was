package exception;

public class InvalidRequestLineException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 request line 입니다. request line = ";

    public InvalidRequestLineException(String path) {
        super(MESSAGE + path);
    }
}
