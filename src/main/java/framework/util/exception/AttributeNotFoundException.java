package framework.util.exception;

public class AttributeNotFoundException  extends InternalServerErrorException {
    public AttributeNotFoundException(String attributeName) {
        super("해당 Attribute '" + attributeName + "'을(를) 찾을 수 없습니다.");
    }
}
