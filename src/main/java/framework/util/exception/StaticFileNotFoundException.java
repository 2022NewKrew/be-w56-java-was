package framework.util.exception;

public class StaticFileNotFoundException extends InternalServerErrorException {
    public StaticFileNotFoundException(String msg) {
        super(msg);
    }

    public StaticFileNotFoundException() {
        super("해당 요청에 맞는 static file을 찾을 수 없습니다.");
    }
}
