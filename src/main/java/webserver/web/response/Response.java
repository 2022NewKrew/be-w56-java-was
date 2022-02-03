package webserver.web.response;

import lombok.extern.slf4j.Slf4j;

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
    private final String cookie;

    private Response(ResponseBuilder builder) {
        this.result = builder.result;
        this.status = builder.status;
        this.contentLength = builder.contentLength;
        this.contentType = builder.contentType;
        this.redirectLocation = builder.redirectLocation;
        this.cookie = builder.cookie;
    }

    private void writeResponseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + status.valueOf() + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            if (!redirectLocation.equals(""))
                dos.writeBytes("Location: " + redirectLocation + "\r\n");
            if (!cookie.equals(""))
                dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos) {
        try {
            dos.write(result, 0, result.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void send(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        writeResponseHeader(dos);
        responseBody(dos);
    }

    public static class ResponseBuilder {
        private byte[] result = "ERROR".getBytes(StandardCharsets.UTF_8);
        private HttpStatus status = HttpStatus.OK;
        private int contentLength = 0;
        private String contentType = "*/*";
        private String redirectLocation = "";
        private String cookie = "";

        public ResponseBuilder() {
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

        public ResponseBuilder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }
    }
}
