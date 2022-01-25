package infrastructure.model;

public enum HttpStatus {

    BAD_REQUEST(400);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
