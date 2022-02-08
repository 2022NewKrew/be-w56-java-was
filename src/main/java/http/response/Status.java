package http.response;

public enum Status {
    OK(200, "OK"),
    FOUND(302, "FOUND"),
    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_FOUND(404, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    private final int code;
    private final String text;

    Status(int code, String text) {
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
