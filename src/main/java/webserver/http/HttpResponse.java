package webserver.http;

import com.google.common.net.HttpHeaders;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
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
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z",
            Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        trailingHeaders.set(HttpHeaders.DATE, dateFormat.format(calendar.getTime()));
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
