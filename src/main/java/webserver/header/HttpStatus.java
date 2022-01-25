package webserver.header;

public enum HttpStatus {
    OK(200),
    FOUND(302);

    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
