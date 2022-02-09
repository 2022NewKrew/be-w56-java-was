package exception;

public class AlreadyLoginException extends RuntimeException {

    private static final String MESSAGE = "이미 로그인한 유저입니다.";

    public AlreadyLoginException() {
        super(MESSAGE);
    }
}
