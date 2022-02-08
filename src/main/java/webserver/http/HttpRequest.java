package webserver.http;

import java.net.URI;

public class HttpRequest {

    private final HttpMethod method;
    private final String requestURI;
    private final HttpVersion version;
    private final HttpHeader headers;
    private final HttpCookie cookies;
    private final String body;

    public HttpRequest(HttpMethod method, String requestURI, String version, HttpHeader headers, HttpCookie cookies, String body) {
        this.method = method;
        this.requestURI = requestURI;
        this.version = HttpVersion.of(version);
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
    }

    public HttpMethod method() {
        return method;
    }

    public URI uri() {
        return URI.create(requestURI);
    }

    public HttpVersion version() {
        return version;
    }

    public HttpHeader headers() {
        return headers;
    }

    public HttpCookie cookies() {
        return cookies;
    }

    public String body() {
        return body;
    }

    @Override
    public String toString() {
        return "MyHttpRequest{" +
                "method='" + method + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                '}';
    }
}
