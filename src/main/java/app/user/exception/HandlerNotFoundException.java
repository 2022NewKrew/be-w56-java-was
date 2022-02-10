package app.user.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(String uri) {
        super("Handler를 찾을 수 없습니다 uri:" + uri);
    }
}
