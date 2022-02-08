package webserver.http;

public class HttpResponse {

    private static final String DEFAULT_VERSION = "HTTP/1.1";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.OK;
    private static final String[] DEFAULT_CONTENT_TYPE = new String[]{"text/html", "charset=utf-8"};

    private final String version;
    private final HttpStatus status;
    private final HttpHeader headers;
    private final HttpCookie cookies;
    private final String[] contentType;
    private final int contentLength;
    private final byte[] body;

    private HttpResponse(Builder builder) {
        this.version = builder.version;
        this.status = builder.status;
        this.headers = builder.headers;
        this.cookies = builder.cookies;
        this.contentType = builder.contentType;
        this.contentLength = getContentLength(builder.body);
        this.body = builder.body;
    }

    private int getContentLength(byte[] body) {
        if (body != null) {
            return body.length;
        }
        return 0;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public HttpCookie getCookies() {
        return cookies;
    }

    public String[] getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public byte[] getBody() {
        return body;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder 클래스
     */
    public static class Builder {

        private final HttpHeader headers = new HttpHeader();
        private final HttpCookie cookies = new HttpCookie();
        private String version = DEFAULT_VERSION;
        private HttpStatus status = DEFAULT_STATUS;
        private String[] contentType = DEFAULT_CONTENT_TYPE;
        private byte[] body;

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder cookie(Cookie cookie) {
            cookies.putCookie(cookie);
            return this;
        }

        public Builder contentType(String... contentTypes) {
            this.contentType = contentTypes;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
