package webserver.core.http.response;

public enum HttpStatus {
    Ok(200, "OK"),
    Redirect(302, "Found"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found");

    private final int statusCode;
    private final String message;

    HttpStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
