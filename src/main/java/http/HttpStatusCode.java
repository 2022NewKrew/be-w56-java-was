package http;

public enum HttpStatusCode {
    SUCCESS(200, "Ok"),
    REDIRECT(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHROIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found");

    private final int statusCode;
    private final String message;

    HttpStatusCode(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode(){ return statusCode; }

    public String getMessage() { return message; }

}
