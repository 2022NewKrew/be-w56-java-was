package webserver.http;

public enum HttpResponseStatus {
    OK("200", "OK"),
    CREATED("201", "Created"),
    NOT_FOUND("404", "Not found"),
    INTERNAL_ERROR("500", "Internal Server Error");

    private final String statusNumber;
    private final String statusMessage;

    HttpResponseStatus(String statusNumber, String statusMessage) {
        this.statusNumber = statusNumber;
        this.statusMessage = statusMessage;
    }

    public String getStatusNumber() {
        return statusNumber;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
