package framework.util.exception;

public class BeanNotFoundException extends InternalServerErrorException {
    public BeanNotFoundException(String msg) {
        super(msg);
    }

    public BeanNotFoundException() {
        super("Bean 등록을 위한 메소드를 찾을 수 없습니다.");
    }
}
