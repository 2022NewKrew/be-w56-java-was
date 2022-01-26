package http;

public enum HttpStatus {
    OK(200),
    FOUND(302),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    NOT_IMPLEMENTED(501);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public String value() {
        return code + " " + name();
    }

    public boolean isClientError() {
        return code >= 400 && code < 500;
    }

    public boolean isServerError() {
        return code >= 500 && code < 600;
    }
}
