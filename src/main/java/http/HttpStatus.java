package http;

public enum HttpStatus {
    OK(200, "OK", ""),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "This HTTP method is not supported by this URL."),
    FOUND(302, "Found", ""),
    PAGE_NOT_FOUND(404, "Not Found", "Server can't find the requested resource."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Internal Server Error.")
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
