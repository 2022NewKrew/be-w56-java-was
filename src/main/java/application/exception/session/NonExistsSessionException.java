package application.exception.session;

public class NonExistsSessionException extends RuntimeException {

    private static final String message = "존재하지 않는 세션입니다.";

    public NonExistsSessionException() {
        super(message);
    }
}
