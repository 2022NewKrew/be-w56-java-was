package exception;

public class InvalidParameterKeyException extends RuntimeException {
    private static final String MESSAGE = "올바른 parameter key 가 아닙니다.";

    public InvalidParameterKeyException() {
        super(MESSAGE);
    }
}
