package framework.util.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String msg) {
        super(msg);
    }

    public InternalServerErrorException() {
        super("처리 중에 오류가 발생하였습니다.");
    }
}
