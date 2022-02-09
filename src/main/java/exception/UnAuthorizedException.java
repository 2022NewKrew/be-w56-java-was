package exception;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("로그인 되지 않은 사용자입니다.");
    }
}
