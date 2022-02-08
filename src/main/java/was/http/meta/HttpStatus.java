package was.http.meta;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SEVER_ERROR(500, "Internal Server Error");

    public final int STATUS;
    public final String MESSAGE;

    HttpStatus(int STATUS, String MESSAGE) {
        this.STATUS = STATUS;
        this.MESSAGE = MESSAGE;
    }
}
