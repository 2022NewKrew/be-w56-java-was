package webserver;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

    private final int statusCode;
    private final String text;

    HttpStatus(int statusCode, String text) {
        this.statusCode = statusCode;
        this.text = text;
    }

    public String getHttpResponseHeader() {
        return "HTTP/1.1 " + statusCode + " " + text + " \r\n";
    }

}
