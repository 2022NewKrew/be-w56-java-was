package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.constant.Http;
import util.constant.Parser;
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
        private byte[] body = Parser.SPACE.getBytes(StandardCharsets.UTF_8);
        private HttpStatus httpStatus = HttpStatus._200;
        private String contentType = "*/*";
        private int contentLength = 0;
        private String redirect = Parser.EMPTY;
        private String cookie = Parser.EMPTY;
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
            responseHeader();
            responseCookie();
            responseBody();
        }

    }

    private void responseHeader() {
        try {
            log.debug("responseHeader write start!");
            dos.writeBytes(Http.VERSION_CURRENT + Parser.SPACE + httpStatus.valueOf() + "\r\n");
            dos.writeBytes(Http.CONTENT_TYPE + Parser.SEMICOLON + Parser.SPACE + contentType + "; charset=utf-8\r\n");
            dos.writeBytes(Http.CONTENT_LENGTH + Parser.SEMICOLON + Parser.SPACE + contentLength + "\r\n");
            dos.writeBytes(Http.LOCATION + Parser.SEMICOLON + redirect + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseCookie() {
        try {
            log.debug("responseHeaderWithCookie write start!");
            dos.writeBytes(Http.COOKIE + Parser.SEMICOLON + Parser.SPACE + Http.COOKIE_LOGINED_KEY + Parser.EQUAL + cookie + ";" + Parser.SPACE + Http.COOKIE_REQUEST_PATH_KEY + Parser.EQUAL + Http.COOKIE_REQUEST_PATH_VALUE + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            log.debug("responseBody write start!");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
