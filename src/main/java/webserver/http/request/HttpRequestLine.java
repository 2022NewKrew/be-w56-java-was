package webserver.http.request;

import java.net.URI;

public class HttpRequestLine {

    private final Method method;
    private final URI uri;
    private final String httpVersion;

    public HttpRequestLine(Method method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public Method getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
