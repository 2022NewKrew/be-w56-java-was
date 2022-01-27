package webserver.http.request;

public enum HttpRequestHeader {
    HOST("HOST"),
    Connection("Connection"),
    Content_Length("Content-Length"),
    Content_Type("Content-Type"),
    Accept("Accept"),
    Cookie("Cookie");

    private final String header;

    HttpRequestHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return header;
    }
}
