package webserver;

public class InvalidHttpRequestException extends Exception {
    public InvalidHttpRequestException(String msg) {
        super(msg);
    }
    public InvalidHttpRequestException(String msg, Exception e) {
        super(msg, e);
    }
}
