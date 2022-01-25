package webserver.response;

public enum Status {
    OK("200", "Ok"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "Not Found");


    private String code;
    private String message;

    Status(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
