package exception;

public class UnsupportedMethodException extends CustomException{
    public UnsupportedMethodException() {
        super(ErrorCode.UNSUPPORTED_METHOD);
    }
}
