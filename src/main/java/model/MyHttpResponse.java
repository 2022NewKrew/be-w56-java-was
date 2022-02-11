package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyHttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);

    private final DataOutputStream dos;
    private final String protocol;
    private final HttpStatus status;
    private final String contentType;
    private final List<String> headers;
    private final byte[] body;

    private static final String DEFAULT_PROTOCOL = "HTTP/1.1";
    private static final String CRLF = "\r\n";

    public MyHttpResponse(Builder builder) {
        this.dos = builder.dos;
        this.protocol = builder.protocol;
        this.status = builder.status;
        this.contentType = builder.contentType;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public void write() {
        try {
            dos.writeBytes(String.format("%s %s %s", protocol, status, CRLF));
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8%s", contentType, CRLF));
            dos.writeBytes(String.format("Content-Length: %s%s", body.length, CRLF));
            writeHeaders(headers);
            dos.writeBytes(CRLF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeHeaders(List<String> headers) throws IOException {
        for (String header : headers) {
            dos.writeBytes(String.format("%s%s", header, CRLF));
        }
    }

    public static class Builder {
        private final DataOutputStream dos;
        private final String protocol = DEFAULT_PROTOCOL;
        private HttpStatus status = HttpStatus.OK;
        private String contentType;
        private final List<String> headers;
        private byte[] body;

        public Builder(DataOutputStream dos) {
            this.dos = dos;
            headers = new ArrayList<>();
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setLocation(String location) {
            this.headers.add(String.format("Location: %s", location));
            return this;
        }

        public Builder setCookie(String cookie) {
            this.headers.add(String.format("Set-Cookie: %s", cookie));
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public MyHttpResponse build() {
            return new MyHttpResponse(this);
        }

    }
}
