package webserver.http;

import com.google.common.net.HttpHeaders;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import webserver.WebServerConfig;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpHeader trailingHeaders;
    private HttpResponseStatus status;
    private byte[] body;

    public HttpResponse() {
        this(WebServerConfig.RESPONSE_HTTP_VERSION);
    }

    public HttpResponse(HttpVersion version) {
        this.version = version;
        trailingHeaders = new HttpHeader();
        setDateHeader();
    }

    private void setDateHeader() {
        final String DATE_HEADER_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_HEADER_PATTERN);
        trailingHeaders.set(HttpHeaders.DATE, dateTimeFormatter.format(ZonedDateTime.now()));
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
