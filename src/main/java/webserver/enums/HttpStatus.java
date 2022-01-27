package webserver.enums;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),

    REDIRECT(302, "Found"),

    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int codeNumber;
    private final String statusCode;

    HttpStatus(int codeNumber, String statusCode) {
        this.codeNumber = codeNumber;
        this.statusCode = statusCode;
    }

    public int getCodeNumber() {
        return codeNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String makeStatusLine(String version) {
        return version + " " + codeNumber + " " + statusCode + "\r\n";
    }
}
