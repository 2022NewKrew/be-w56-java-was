package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String DEFAULT_VERSION = "HTTP/1.1";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.OK;
    private static final String[] DEFAULT_CONTENT_TYPE = new String[]{"text/html", "charset=utf-8"};
    private static final String CRLF = "\r\n";

    private final DataOutputStream dos;
    private final String version;
    private final HttpStatus status;
    private final Map<String, String> headers;
    private final List<HttpCookie> cookies;
    private final String[] contentType;
    private final int contentLength;
    private final byte[] body;

    private HttpResponse(Builder builder) {
        this.dos = builder.dos;
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

    public static Builder builder(DataOutputStream dos) {
        return new Builder(dos);
    }

    public void writeBytes() {
        String contentType = String.join(";", this.contentType);
        try {
            dos.writeBytes(String.format("%s %s %s", version, status, CRLF));
            writeBytesHeaders();
            dos.writeBytes(String.format("Content-Type: %s%s", contentType, CRLF));
            writeBytesCookies();
            dos.writeBytes(String.format("Content-Length: %s%s", contentLength, CRLF));
            dos.writeBytes(CRLF);
            dos.write(body, 0, contentLength);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeBytesHeaders() throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(String.format("%s: %s%s", key, headers.get(key), CRLF));
        }
    }

    private void writeBytesCookies() throws IOException {
        for (HttpCookie cookie : cookies) {
            dos.writeBytes(String.format("Set-Cookie: %s%s", cookie, CRLF));
        }
    }

    public void flush() {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Builder 클래스
     */
    public static class Builder {

        private final DataOutputStream dos;
        private final Map<String, String> headers = new HashMap<>();
        private final List<HttpCookie> cookies = new ArrayList<>();
        private String version = DEFAULT_VERSION;
        private HttpStatus status = DEFAULT_STATUS;
        private String[] contentType = DEFAULT_CONTENT_TYPE;
        private byte[] body;

        public Builder(DataOutputStream dos) {
            this.dos = dos;
        }

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

        public Builder cookie(HttpCookie cookie) {
            cookies.add(cookie);
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
