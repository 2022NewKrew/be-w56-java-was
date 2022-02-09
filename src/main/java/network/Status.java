package network;

public enum Status {
    OK(200, "HTTP/1.1 200 OK \r\n"),
    FOUND(302, "HTTP/1.1 302 Found \r\n"),
    UNAUTHORIZED(401, "HTTP/1.1 401 Unauthorized"), NOTFOUND(404, "HTTP/1.1 404 Not Found");

    private int value;
    private String message;

    Status(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
