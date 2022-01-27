package application.exception.user;

public class NonExistsUserIdException extends RuntimeException {

    private static final String message = "존재하지 않는 아이디입니다.";

    public NonExistsUserIdException() {
        super(message);
    }
}
