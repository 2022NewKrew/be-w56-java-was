package webserver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import webserver.web.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// TODO delete getter
@Slf4j
public class Response {

    private final byte[] result;
    private final HttpStatus status;
    private final int contentLength;
    private final String contentType;
    private final String redirectLocation;
    private final DataOutputStream dos;

    private Response(ResponseBuilder builder) {
        this.result = builder.result;
        this.status = builder.status;
        this.contentLength = builder.contentLength;
        this.contentType = builder.contentType;
        this.redirectLocation = builder.redirectLocation;
        this.dos = new DataOutputStream(builder.out);
    }

    private void writeResponseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 " + status.valueOf() + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.write(result, 0, result.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void send() {
        writeResponseHeader();
        responseBody();
    }

    public static class ResponseBuilder {
        private byte[] result = "ERROR".getBytes(StandardCharsets.UTF_8);
        private HttpStatus status = HttpStatus.OK;
        private int contentLength = 0;
        private String contentType = "*/*";
        private final OutputStream out;
        private String redirectLocation = "";

        public ResponseBuilder(OutputStream out) {
            this.out = out;
        }

        public ResponseBuilder setResult(byte[] result) {
            this.result = result;
            return this;
        }

        public ResponseBuilder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ResponseBuilder setContentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public ResponseBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public ResponseBuilder setRedirectLocation(String location) {
            this.redirectLocation = location;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
