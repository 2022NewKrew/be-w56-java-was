package webserver;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String msg) {
        super(msg);
    }
    public DuplicateUserException(String msg, Exception e) {
        super(msg, e);
    }
}
