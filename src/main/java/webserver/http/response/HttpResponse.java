package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.ContentType;
import webserver.http.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final byte[] body;
    private final HttpStatus httpStatus;
    private final String contentType;
    private final int contentLength;
    private final String redirect;
    private final DataOutputStream dos;

    private HttpResponse(Builder builder) {
        this.body = builder.body;
        this.httpStatus = builder.httpStatus;
        this.contentType = builder.contentType;
        this.contentLength = builder.contentLength;
        this.redirect = builder.redirect;
        this.dos = new DataOutputStream(builder.out);
    }

    public static class Builder {
        private byte[] body = " ".getBytes(StandardCharsets.UTF_8);
        private HttpStatus httpStatus = HttpStatus._200;
        private String contentType = "*/*";
        private int contentLength = 0;
        private String redirect = "";
        private final OutputStream out;

        public Builder(OutputStream out) {
            this.out = out;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setContentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public Builder setRedirect(String redirect) {
            this.redirect = redirect;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public void write() {
        log.debug("HttpResponse write start!");
        responseHeader();
        responseBody();
    }

    private void responseHeader() {
        try {
            log.debug("responseHeader write start!");
            dos.writeBytes(Constants.HTTP_VERSION_CURRENT + Constants.SPACE + httpStatus.valueOf() + "\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_TYPE + Constants.SEMICOLON + Constants.SPACE + contentType + "; charset=utf-8\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_LENGTH + Constants.SEMICOLON + Constants.SPACE + contentLength + "\r\n");
            dos.writeBytes(Constants.HTTP_LOCATION + redirect + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            log.debug("responseBody write start!");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
