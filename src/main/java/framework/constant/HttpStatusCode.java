package framework.constant;

public enum HttpStatusCode {
    OK(200, "OK"),
    REDIRECT(302, "Found");

    private final int statusCode;
    private final String msg;

    HttpStatusCode(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
