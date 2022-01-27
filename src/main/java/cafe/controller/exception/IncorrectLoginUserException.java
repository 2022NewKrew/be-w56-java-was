package cafe.controller.exception;

public class IncorrectLoginUserException extends Exception{
    public IncorrectLoginUserException() {
        super("Incorrect Login User");
    }
}
