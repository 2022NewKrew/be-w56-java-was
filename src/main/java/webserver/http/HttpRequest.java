package webserver.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.*;

public class HttpRequest {

    private final HttpMethod method;
    private final String requestURI;
    private final String version;
    private final Map<String, List<String>> headers;
    private final Map<String, HttpCookie> cookies;
    private final String body;

    public HttpRequest(HttpMethod method, String requestURI, String version, Map<String, List<String>> headers, Map<String, HttpCookie> cookies, String body) {
        this.method = method;
        this.requestURI = requestURI;
        this.version = version;
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
    }

    public String method() {
        return method.name();
    }

    public URI uri() {
        return URI.create(requestURI);
    }

    public Optional<HttpClient.Version> version() {
        if (version.equals("HTTP/1.1")) {
            return Optional.of(HttpClient.Version.HTTP_1_1);
        }
        if (version.equals("HTTP/2.0")) {
            return Optional.of(HttpClient.Version.HTTP_2);
        }
        return Optional.empty();
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Map<String, HttpCookie> cookies() {
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
