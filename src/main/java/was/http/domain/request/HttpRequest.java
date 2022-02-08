package was.http.domain.request;

import java.util.Map;

public class HttpRequest {
    public static final Builder builder = new Builder();

    private final MethodAndPath methodAndPath;
    private final String version;

    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final Map<String, String> requestParams;
    private final Map<String, String> pathVariables;

    private final Cookie cookie;

    private final String requestBody;

    public void setPath(String path) {
        methodAndPath.setPath(path);
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getRequestParam(String key) {
        return requestParams.get(key);
    }

    public String getQueryParam(String key) {
        return queryParams.get(key);
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getPath() {
        return methodAndPath.getPath();
    }

    public boolean isNotStaticResource() {
        return !getPath().startsWith("/statics/") && !getPath().equals("/favicon.ico");
    }

    public String getMethod() {
        return methodAndPath.getMethod();
    }

    public Cookie getCookie() {
        return cookie;
    }

    protected HttpRequest(String method, String path, String version, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> requestParams, Map<String, String> pathVariables, String requestBody, Cookie cookie) {
        this.methodAndPath = new MethodAndPath(method, path);
        this.version = version;
        this.headers = headers;
        this.queryParams = queryParams;
        this.requestParams = requestParams;
        this.pathVariables = pathVariables;
        this.requestBody = requestBody;
        this.cookie = cookie;
    }

    public static class Builder {
        private String method;
        private String path;
        private String version;

        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private Map<String, String> requestParams;
        private Map<String, String> pathVariables;

        private String requestBody;

        private Cookie cookie;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder requestParams(Map<String, String> requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public Builder pathVariables(Map<String, String> pathVariables) {
            this.pathVariables = pathVariables;
            return this;
        }

        public Builder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder cookie(Cookie cookie) {
            this.cookie = cookie;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(method, path, version, headers, queryParams, requestParams, pathVariables, requestBody, cookie);
        }
    }

    public MethodAndPath getMethodAndPath() {
        return methodAndPath;
    }
}
