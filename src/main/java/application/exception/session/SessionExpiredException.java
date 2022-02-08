package application.exception.session;

public class SessionExpiredException extends RuntimeException {

    private static final String message = "만료된 세션입니다.";

    public SessionExpiredException() {
        super(message);
    }
}
