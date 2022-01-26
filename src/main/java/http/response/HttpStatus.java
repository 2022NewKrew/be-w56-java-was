package http.response;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    private final int code;
    private final String text;

    HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
