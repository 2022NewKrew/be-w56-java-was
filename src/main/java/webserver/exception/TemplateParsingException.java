package webserver.exception;

public class TemplateParsingException extends RuntimeException {
    public TemplateParsingException() {
    }

    public TemplateParsingException(String message) {
        super(message);
    }
}
