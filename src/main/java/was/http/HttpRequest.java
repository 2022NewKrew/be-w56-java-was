package was.http;

import was.meta.HttpVersion;

import java.util.Map;

public class HttpRequest {

    private final HttpVersion version;
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Cookie cookie;
    private final String body;
    private final Map<String, String> requestParams;

    private HttpRequest(HttpVersion version, String method, String path, Map<String, String> queryParams, Map<String, String> headers, Cookie cookie, String body, Map<String, String> requestParams) {
        this.version = version;
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.headers = headers;
        this.cookie = cookie;
        this.body = body;
        this.requestParams = requestParams;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private HttpVersion version;
        private String method;
        private String path;
        private Map<String, String> queryParams;
        private Map<String, String> headers;
        private Cookie cookie;
        private String body;
        private Map<String, String> requestParams;

        public Builder version(HttpVersion version) {
            this.version = version;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder cookie(Cookie cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder requestParams(Map<String, String> requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(version, method, path, queryParams, headers, cookie, body, requestParams);
        }
    }

    public HttpVersion getVersion() {
        return version;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryParam(String key) {
        return queryParams.get(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getCookie(String key) {
        return cookie.getAttribute(key);
    }

    public String getBody() {
        return body;
    }

    public String getRequestParam(String key) {
        return requestParams.get(key);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(method).append(" ")
                .append(path).append(" ")
                .append(queryParams.toString()).append(" ")
                .append(version.getValue()).append("\r\n")
                .append(headers.toString()).append("\r\n")
                .append(cookie.toString()).append("\r\n")
                .append(body).append("\r\n").toString();
    }
}
