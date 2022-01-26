package http;

public enum HttpStatus {

    OK(200, "OK"),

    REDIRECT(302, "FOUND"),

    NOT_FOUND(404, "NOT FOUND");

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
