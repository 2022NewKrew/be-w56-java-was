package httpmodel;

public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302, "Found");

    private final int status;
    private final String statusMessage;

    HttpStatus(int httpStatus, String statusMessage) {
        this.status = httpStatus;
        this.statusMessage = statusMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
