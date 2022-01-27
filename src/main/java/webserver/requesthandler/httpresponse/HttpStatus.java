package webserver.requesthandler.httpresponse;

public enum HttpStatus {
    OK(200),
    FOUND(302);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name();
    }
}
