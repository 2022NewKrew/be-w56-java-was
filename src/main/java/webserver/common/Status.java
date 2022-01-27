package webserver.common;

public enum Status {
    OK("200", "Ok"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "Not Found"),
    NOT_ALLOWED("405", "Method Not Allowed");


    private final String code;
    private final String message;

    Status(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCodeAndMessage() {
        return String.format("%s %s", this.code, this.message);
    }
}
