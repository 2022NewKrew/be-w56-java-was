package webserver.http.request;

public class HttpRequestLine {

    private final String HttpVersion;
    private final String method;
    private final String uri;

    public HttpRequestLine(String method, String uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        HttpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return HttpVersion;
    }

    enum Method {
        GET("GET"),
        POST("POST");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return method;
        }
    }
}
