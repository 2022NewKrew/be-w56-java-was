package http;

public enum HttpStatus {
    OK("200", "OK"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "NotFound");

    private final String statusCode;
    private final String reasonPhase;

    HttpStatus(String statusCode, String reasonPhase) {
        this.statusCode = statusCode;
        this.reasonPhase = reasonPhase;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getReasonPhase() {
        return reasonPhase;
    }
}
