package webserver.http.request;

import util.constant.Http;

import java.util.Map;
import java.util.Objects;

public class HttpRequestLine {
    private final String method;
    private final String url;
    private final String httpVersion;

    private HttpRequestLine(String method, String url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestLine of(Map<String, String> line) {
        if (line == null) {
            throw new IllegalArgumentException("invalid input: HttpRequestLine cannot be null.");
        }
        return new HttpRequestLine(line.get(Http.METHOD), line.get(Http.URL), line.get(Http.VERSION));
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestLine that = (HttpRequestLine) o;
        return Objects.equals(method, that.method) && Objects.equals(url, that.url) && Objects.equals(httpVersion, that.httpVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url, httpVersion);
    }

    @Override
    public String toString() {
        return "HttpRequestLine{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
