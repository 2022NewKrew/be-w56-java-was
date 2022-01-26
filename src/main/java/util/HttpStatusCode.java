package util;

public enum HttpStatusCode {
    OK(200),
    FOUND(302);

    private final int statusCode;

    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
