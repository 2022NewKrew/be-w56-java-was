package framework.util.exception;

public class ComponentNotFoundException extends InternalServerErrorException {
    public ComponentNotFoundException(String msg) {
        super(msg);
    }

    public ComponentNotFoundException() {
        super("등록을 위한 Component 클래스의 생성자를 찾을 수 없습니다.");
    }
}
