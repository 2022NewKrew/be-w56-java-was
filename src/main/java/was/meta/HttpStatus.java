package was.meta;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    FORBIDDEN(403, "Forbidden");

    public final int STATUS;
    public final String MESSAGE;

    HttpStatus(int STATUS, String MESSAGE) {
        this.STATUS = STATUS;
        this.MESSAGE = MESSAGE;
    }
}
