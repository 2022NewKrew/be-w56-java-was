package webserver.http.response;

public enum HttpResponseHeader {
    Content_Type("Content-Type"),
    Content_Length("Content-Length"),
    Location("Location"),
    Set_Cookie("Set-Cookie");

    private final String header;

    HttpResponseHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return header;
    }
}
