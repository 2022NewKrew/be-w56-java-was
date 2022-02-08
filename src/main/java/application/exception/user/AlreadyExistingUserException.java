package application.exception.user;

public class AlreadyExistingUserException extends RuntimeException {

    private static final String message = "존재하지 않는 아이디입니다.";

    public AlreadyExistingUserException() {
        super(message);
    }
}
