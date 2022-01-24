package webserver.http;

public enum HttpStatus {
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404);

    private final int statusCode;
    HttpStatus(int statusCode){
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
