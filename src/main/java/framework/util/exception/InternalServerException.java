package framework.util.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String msg) {
        super(msg);
    }

    public InternalServerException() {
        super("처리 중에 오류가 발생하였습니다.");
    }
}
