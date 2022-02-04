package framework.util.exception;

public class WrongReturnTypeException extends InternalServerErrorException {
    public WrongReturnTypeException(String msg) {
        super(msg);
    }

    public WrongReturnTypeException() {
        super("Controller로부터 반환받은 변수의 타입이 String형 또는 ModelView형이 아닙니다.");
    }
}
