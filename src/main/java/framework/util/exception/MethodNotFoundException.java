package framework.util.exception;

public class MethodNotFoundException extends InternalServerErrorException {
    public MethodNotFoundException(String msg) {
        super(msg);
    }

    public MethodNotFoundException() {
        super("해당 요청에 맞는 Controller 클래스에서 알맞는 메소드를 찾을 수 없습니다.");
    }
}
