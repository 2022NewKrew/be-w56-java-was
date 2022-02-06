package app.user.exception;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("패스워드가 일치하지 않습니다");
    }
}
