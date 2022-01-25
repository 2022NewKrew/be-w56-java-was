package http;

public enum StatusCode {
    OK(200),
    FOUND(302),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
