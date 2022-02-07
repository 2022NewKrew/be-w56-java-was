package webserver.http;

import com.google.common.net.HttpHeaders;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServerConfig;
import webserver.util.HttpResponseUtil;

public class HttpResponse {

    private final OutputStream out;
    private final HttpVersion version;
    private final HttpHeader trailingHeaders;
    private HttpResponseStatus status;
    private byte[] body;

    public HttpResponse(OutputStream out) {
        this(out, WebServerConfig.RESPONSE_HTTP_VERSION);
    }

    public HttpResponse(OutputStream out, HttpVersion version) {
        this.out = out;
        this.version = version;
        trailingHeaders = new HttpHeader();
        setDateHeader();
    }

    public void send() {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HttpResponseUtil.responseLineString(this));
            dos.writeBytes(HttpResponseUtil.headerString(this));
            if (body != null && body.length > 0) {
                dos.write(HttpResponseUtil.bodyString(this).getBytes(StandardCharsets.UTF_8));
            }
            dos.flush();
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(HttpResponse.class);
            logger.error(e.getMessage());
        }
    }

    private void setDateHeader() {
        final String DATE_HEADER_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_HEADER_PATTERN);
        trailingHeaders.set(HttpHeaders.DATE, dateTimeFormatter.format(ZonedDateTime.now()));
    }

    public void setCookie(String cookieValue) {
        headers().set(HttpHeaders.COOKIE, cookieValue);
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpHeader headers() {
        return trailingHeaders;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public HttpResponse setStatus(HttpResponseStatus status) {
        this.status = status;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] contents) {
        this.body = contents;
    }
}
