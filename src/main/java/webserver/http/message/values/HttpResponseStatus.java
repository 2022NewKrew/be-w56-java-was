package webserver.http.message.values;

public enum HttpResponseStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    ;

    private final int statusCode;
    private final String reasonPhrase;

    HttpResponseStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
