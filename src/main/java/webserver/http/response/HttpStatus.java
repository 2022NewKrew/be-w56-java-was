package webserver.http.response;

public enum HttpStatus {
    _200("200 OK"),
    _201("201 Created"),
    _202("202 Accepted"),
    _203("203 Non-Authoritative Information"),
    _204("204 No Content"),
    _302("302 Found"),
    _403("403 Forbidden"),
    _404("404 Not Found");

    private final String status;

    HttpStatus(String status) {
        this.status = status;
    }

    public String valueOf() {
        return status;
    }

    @Override
    public String toString() {
        return "HttpStatus{" +
                "status='" + status + '\'' +
                '}';
    }
}
