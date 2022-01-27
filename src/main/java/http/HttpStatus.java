package http;

public enum HttpStatus {
    OK("200", "OK"),
    FOUND("302", "Found"),
    SEE_OTHER("303", "See Other"),
    NOT_FOUND("404", "Not Found"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private String code;

    private String text;

    HttpStatus(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
