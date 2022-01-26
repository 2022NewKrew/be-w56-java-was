package infrastructure.model;

public enum HttpStatus {

    OK(200, "Ok"),
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String name;

    HttpStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getStartLineMessage() {
        return code + " " + name;
    }
}
