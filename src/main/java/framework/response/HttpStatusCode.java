package framework.response;

public enum HttpStatusCode {
    OK(200, "OK"),
    REDIRECT(302, "Found");

    private int statusCode;
    private String msg;

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
