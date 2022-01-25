package http;

public enum HttpStatus {
    OK(200, "OK", ""),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "This HTTP method is not supported by this URL."),
    NOT_IMPLEMENTED(501, "Not Implemented", "The server is unable to process your request."),
    ;

    private final int code;
    private final String description;
    private final String errorMessage;

    HttpStatus(int code, String description, String errorMessage) {
        this.code = code;
        this.description = description;
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return code + " " + description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
