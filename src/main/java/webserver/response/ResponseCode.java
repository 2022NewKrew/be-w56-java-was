package webserver.response;

public enum ResponseCode {
    STATUS_200(200, "OK"),
    STATUS_302(302, "Found"),
    STATUS_303(303, "See Other"),
    STATUS_403(403, "Forbidden"),
    STATUS_404(404, "Not Found");

    int statusCode;
    String statusName;

    ResponseCode(int statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusName() {
        return statusName;
    }
}
