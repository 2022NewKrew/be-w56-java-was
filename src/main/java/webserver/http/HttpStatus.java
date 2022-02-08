package webserver.http;

public enum HttpStatus {
    OK("200 OK"),
    FOUND("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found"),
    UNAUTHORIZED("401 Unauthorized");

    final String httpStatus;

    HttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return httpStatus;
    }
}
