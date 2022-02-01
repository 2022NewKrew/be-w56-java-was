package framework.util.exception;

public class ConstructorNotFoundException extends InternalServerErrorException {
    public ConstructorNotFoundException(String msg) {
        super(msg);
    }

    public ConstructorNotFoundException() {
        super("해당 클래스에서 원하는 생성자를 찾을 수 없습니다.");
    }
}