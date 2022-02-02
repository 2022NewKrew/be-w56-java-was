package network;

public enum HttpStatus {
    // 2xx
    OK("200 OK"),
    CREATED("201 Created"),
    ACCEPTED("202 Accepted"),
    NO_CONTENT("204 No Content"),

    // 3xx
    FOUND("302 Found"),

    // 4xx
    BAD_REQUEST("400 Bad Request"),
    UNAUTHORIZED("401 Unauthorized"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    METHOD_NOT_ALLOWED("405 Method Not Allowed"),
    CONFLICT("409 Conflict"),

    // 5xx
    INTERNAL_SERVER_ERROR("500 Internal Server Error");

    private final String statusLine;

    HttpStatus(String statusLine) {
        this.statusLine = statusLine;
    }

    public String getStatusLine() {
        return "HTTP/1.1 " + statusLine + " \r\n";
    }
}
