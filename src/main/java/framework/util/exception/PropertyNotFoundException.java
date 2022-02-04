package framework.util.exception;

public class PropertyNotFoundException extends InternalServerErrorException {
    public PropertyNotFoundException(String property) {
        super("해당 ModelView Attribute에서 해당 Property '" + property + "'을(를) 찾을 수 없습니다.");
    }
}
