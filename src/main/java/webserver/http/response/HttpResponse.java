package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import webserver.http.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final byte[] body;
    private final HttpStatus httpStatus;
    private final String contentType;
    private final int contentLength;
    private final String redirect;
    private final String cookie;
    private final DataOutputStream dos;

    private HttpResponse(Builder builder) {
        this.body = builder.body;
        this.httpStatus = builder.httpStatus;
        this.contentType = builder.contentType;
        this.contentLength = builder.contentLength;
        this.redirect = builder.redirect;
        this.cookie = builder.cookie;
        this.dos = new DataOutputStream(builder.out);
    }

    public static class Builder {
        private byte[] body = " ".getBytes(StandardCharsets.UTF_8);
        private HttpStatus httpStatus = HttpStatus._200;
        private String contentType = "*/*";
        private int contentLength = 0;
        private String redirect = "";
        private String cookie = "";
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

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public void write() {
        log.debug("HttpResponse write start!");

        if (Objects.equals(this.cookie, "")) {
            responseHeader();
            responseBody();
        } else {
            responseHeaderWithCookie();
            responseBody();
        }

    }

    private void responseHeader() {
        try {
            log.debug("responseHeader write start!");
            dos.writeBytes(Constants.HTTP_VERSION_CURRENT + Constants.SPACE + httpStatus.valueOf() + "\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_TYPE + Constants.SEMICOLON + Constants.SPACE + contentType + "; charset=utf-8\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_LENGTH + Constants.SEMICOLON + Constants.SPACE + contentLength + "\r\n");
            dos.writeBytes(Constants.HTTP_LOCATION + Constants.SEMICOLON + redirect + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeaderWithCookie() {
        try {
            log.debug("responseHeaderWithCookie write start!");
            dos.writeBytes(Constants.HTTP_VERSION_CURRENT + Constants.SPACE + httpStatus.valueOf() + "\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_TYPE + Constants.SEMICOLON + Constants.SPACE + contentType + "; charset=utf-8\r\n");
            dos.writeBytes(Constants.HTTP_CONTENT_LENGTH + Constants.SEMICOLON + Constants.SPACE + contentLength + "\r\n");
            dos.writeBytes(Constants.HTTP_LOCATION + Constants.SEMICOLON + redirect + "\r\n");
            dos.writeBytes(Constants.HTTP_COOKIE + Constants.SEMICOLON + Constants.SPACE + Constants.HTTP_COOKIE_LOGINED_KEY + Constants.EQUAL + cookie + ";" + Constants.SPACE + Constants.HTTP_COOKIE_REQUEST_PATH_KEY + Constants.EQUAL + Constants.HTTP_COOKIE_REQUEST_PATH_VALUE + "\r\n");
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
