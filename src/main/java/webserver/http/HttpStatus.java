package webserver.http;

public enum HttpStatus {

    // 200
    OK(200, "200 OK"),
    CREATED(201, "201 Created"),

    // 300
    FOUND(302, "302 Found"),

    // 400
    BAD_REQUEST(400, "400 Bad Request"),
    UNAUTHORIZED(401, "401 Unauthorized"),
    FORBIDDEN(403, "403 Forbidden"),
    NOT_FOUND(404, "404 Not Found"),
    METHOD_NOT_ALLOWED(405, "405 Method Not Allowed"),

    // 500
    INTERNAL_SERVER_ERROR(500, "500 Internal Server Error");


    final int code;
    final String status;

    HttpStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
