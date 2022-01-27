package http.request;

import http.HttpMethod;
import http.Url;

import java.io.BufferedReader;
import java.io.IOException;

public class StartLine {

    private HttpMethod method;

    private Url url;

    private String httpVersion;

    public StartLine(HttpMethod method, Url url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public static StartLine of(HttpMethod method, Url url, String httpVersion) {
        return new StartLine(method, url, httpVersion);
    }

    public HttpMethod getHttpMethod() {
        return method;
    }

    public Url getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
