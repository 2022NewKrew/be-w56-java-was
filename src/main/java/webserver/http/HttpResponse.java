package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final String DEFAULT_VERSION = "HTTP/1.1";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.OK;
    private static final String[] DEFAULT_CONTENT_TYPE = new String[]{"text/html", "charset=utf-8"};

    private final Map<String, String> headers;
    private final String version;
    private final HttpStatus statusCode;
    private final String[] contentType;
    private final byte[] body;

    private HttpResponse(Builder builder) {
        this.headers = builder.headers;
        this.version = builder.version;
        this.statusCode = builder.statusCode;
        this.contentType = builder.contentType;
        this.body = builder.body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String[] getContentType() {
        return contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public int getContentLength() {
        if (this.body != null) {
            return this.body.length;
        }
        return 0;
    }

    public static class Builder {
        private Map<String, String> headers = new HashMap<>();
        private String version = DEFAULT_VERSION;
        private HttpStatus statusCode = DEFAULT_STATUS;
        private String[] contentType = DEFAULT_CONTENT_TYPE;
        private byte[] body;

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder status(HttpStatus statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder contentType(String... contentType) {
            this.contentType = contentType;
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
