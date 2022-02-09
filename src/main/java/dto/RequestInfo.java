package dto;

import java.util.Map;

public class RequestInfo {

    private final String requestMethod;
    private final String requestPath;
    private final String version;
    private final Map<String, String> headers;
    private final Map<String, String> cookies;
    private final Map<String, String> queryParams;
    private final Map<String, String> bodyParams;

    private RequestInfo(String requestMethod, String requestPath, String version, Map<String, String> headers, Map<String, String> cookies, Map<String, String> queryParams, Map<String, String> bodyParams) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.version = version;
        this.headers = headers;
        this.cookies = cookies;
        this.queryParams = queryParams;
        this.bodyParams = bodyParams;
    }

    public String getRequestMethod() { return this.requestMethod; }
    public String getRequestPath() { return this.requestPath; }
    public String getVersion() { return this.version; }
    public Map<String, String> getHeaders() { return this.headers; }
    public Map<String, String> getCookies() { return this.cookies; }
    public Map<String, String> getQueryParams() { return this.queryParams; }
    public Map<String, String> getBodyParams() { return this.bodyParams; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String requestMethod;
        private String requestPath;
        private String version;
        private Map<String, String> headers;
        private Map<String, String> cookies;
        private Map<String, String> queryParams;
        private Map<String, String> bodyParams;

        private Builder() {}

        public Builder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder requestPath(String requestPath) {
            this.requestPath = requestPath;
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

        public Builder cookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder bodyParams(Map<String, String> bodyParams) {
            this.bodyParams = bodyParams;
            return this;
        }

        public RequestInfo build() {
            return new RequestInfo(
                    this.requestMethod,
                    this.requestPath,
                    this.version,
                    this.headers,
                    this.cookies,
                    this.queryParams,
                    this.bodyParams
            );
        }
    }
}
