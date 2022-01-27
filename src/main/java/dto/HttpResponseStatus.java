package dto;


public enum HttpResponseStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    NOT_FOUND(404, "NOT FOUND");

    private final int statusCode;
    private final String message;

    private HttpResponseStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() { return this.statusCode; }
    public String getMessage() { return this.message; }
}
