package http;

public enum HttpStatus {
    OK (200),
    Found (301),
    NotFound(404),
    InternalServerError(500);

    private final int statusCode;
    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
