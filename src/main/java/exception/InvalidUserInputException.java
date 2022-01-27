package exception;

public class InvalidUserInputException extends RuntimeException {

    private static final String MESSAGE = "사용자 입력이 유효하지 않습니다.";

    public InvalidUserInputException() {
        super(MESSAGE);
    }

    public InvalidUserInputException(String message) {
        super(message);
    }
}
