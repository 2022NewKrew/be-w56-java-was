package app.user.exception;

public class NonIterableException extends RuntimeException {

    public NonIterableException() {
        super("Iterable하지 않은 값 입니다.");
    }
}
