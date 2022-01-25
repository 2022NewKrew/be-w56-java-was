package exception;

public class UnsupportedHttpMethodException extends CustomException{
    public UnsupportedHttpMethodException() {
        super(ErrorCode.UNSUPPORTED_METHOD);
    }
}
