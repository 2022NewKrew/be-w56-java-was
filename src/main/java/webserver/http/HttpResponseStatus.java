package webserver.http;

public enum HttpResponseStatus {
    OK("200", "OK"),
    CREATED("201", "Created"),
    ;

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
