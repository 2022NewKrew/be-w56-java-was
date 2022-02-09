package application.exception.memo;

public class NonExistsMemoIdException extends RuntimeException {

    private static final String message = "존재하지 않는 메모입니다모";

    public NonExistsMemoIdException() { super(message); }
}
