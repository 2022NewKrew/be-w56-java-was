package app.util.exception;

public class TemplateSyntaxException extends RuntimeException {
    public TemplateSyntaxException() {
        super();
    }

    public TemplateSyntaxException(String message) {
        super(message);
    }
}
