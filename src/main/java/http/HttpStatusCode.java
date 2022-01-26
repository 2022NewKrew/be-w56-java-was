package http;

public enum HttpStatusCode {
    STATUS_CODE_200(200, "OK"),
    STATUS_CODE_302(302, "Found"),
    STATUS_CODE_404(404, "Not Found"),
    STATUS_CODE_500(500, "Internal Server Error");

    private final String status;
    private final int code;

    HttpStatusCode(int code, String status) {
        this.code = code;
        this.status = status;
    }
    @Override
    public String toString() {
        return code + " " + status;
    }
}
