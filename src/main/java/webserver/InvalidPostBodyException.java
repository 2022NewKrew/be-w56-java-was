package webserver;

public class InvalidPostBodyException extends Exception {
    public InvalidPostBodyException(String msg) {
        super(msg);
    }
    public InvalidPostBodyException(String msg, Exception e) {
        super(msg, e);
    }
}
