package webserver.model;

public class HttpRequestHeader {
    private String protocol;
    private String method;
    private String requestURI;

    public HttpRequestHeader(String method, String requestURI, String protocol) {
        this.method = method;
        this.requestURI = requestURI;
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

}
