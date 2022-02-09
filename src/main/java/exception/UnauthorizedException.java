package exception;

public class UnauthorizedException extends RuntimeException {

    private static final String MESSAGE = "인증되지 않은 요청입니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }
}
