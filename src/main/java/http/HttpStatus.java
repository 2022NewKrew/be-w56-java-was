package http;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "FOUND"),
    NOT_FOUND(404, "NOT FOUND");

    private final int statusCode;
    private final String statusText;

    HttpStatus(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

}
