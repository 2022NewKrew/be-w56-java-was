package framework.util.exception;

public class CannotFillContainerException  extends InternalServerErrorException {
    public CannotFillContainerException(String msg) {
        super(msg);
    }

    public CannotFillContainerException() {
        super("Container를 채우는 도중 오류가 발생하였습니다.");
    }
}
