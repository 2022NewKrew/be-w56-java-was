package http;

import java.net.URI;

public class HttpRequestLine {
    private final String method;
    private final URI uri;
    private final HttpVersion version;

    protected HttpRequestLine(String method, URI uri, HttpVersion version) {
        this.method = method;
        this.uri = uri;
        this.version = version;
    }

    public static HttpRequestLine from(String line) {
        String[] tokens = line.split(" ");
        URI uri = URI.create(tokens[1]);
        HttpVersion version = HttpVersion.fromString(tokens[2]);
        return new HttpRequestLine(tokens[0], uri, version);
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public HttpVersion getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpRequestLine)) {
            return false;
        }
        HttpRequestLine other = (HttpRequestLine) obj;
        return method.equals(other.method)
                && uri.equals(other.uri)
                && version.equals(other.version);
    }

    @Override
    public String toString() {
        return "Method: " + method + ", Uri: " + uri + ", Version: " + version;
    }
}
