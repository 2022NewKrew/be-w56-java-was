package framework.util.exception;

public class ClassNotFoundException extends InternalServerErrorException {
    public ClassNotFoundException(String msg) {
        super(msg);
    }

    public ClassNotFoundException() {
        super("해당 요청에 맞는 Controller 클래스를 찾을 수 없습니다.");
    }
}
