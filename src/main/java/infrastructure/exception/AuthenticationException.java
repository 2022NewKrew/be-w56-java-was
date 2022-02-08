package infrastructure.exception;

public class AuthenticationException extends RuntimeException {

    private final static String message = "로그인이 필요합니다.";

    public AuthenticationException() {
        super(message);
    }
}
