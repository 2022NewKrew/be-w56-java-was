package was.http.domain.response;

import was.http.domain.request.Cookie;
import was.http.domain.request.HttpRequest;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final String version;
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public static HttpResponse of(HttpRequest httpRequest) {
        return new HttpResponse(httpRequest.getVersion(), null, null);
    }

    public boolean isRedirect() {
        return headers.get(HttpHeaders.LOCATION) != null;
    }

    public boolean hasNotContentType() {
        return headers.get(HttpHeaders.CONTENT_TYPE) == null;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void initHeaders() {
        headers.clear();
    }

    public void addAllHeader(Map<String, String> viewHeaders) {
        viewHeaders.forEach((key, value) -> {
            if (!headers.containsKey(key)) {
                headers.put(key, value);
            }
        });
    }

    protected HttpResponse(String version, HttpStatus status, byte[] body) {
        this.version = version;
        this.status = status;
        this.body = body;
    }

    public int getContentLength() {
        if (body == null) {
            return 0;
        }
        return body.length;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void addCookie(Cookie cookie) {
        headers.put(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toHttp() {
        final StringBuilder sb = new StringBuilder();

        sb.append(version)
                .append(' ')
                .append(status.STATUS)
                .append(' ')
                .append(status.MESSAGE)
                .append("\r\n");

        headers.forEach((key, value) -> sb.append(key).append(':').append(' ').append(value).append("\r\n"));
        sb.append("\r\n");

        final byte[] headerLine = sb.toString().getBytes(StandardCharsets.UTF_8);

        if (body == null) {
            return headerLine;
        }

        final byte[] result = new byte[headerLine.length + body.length];
        System.arraycopy(headerLine, 0, result, 0, headerLine.length);
        System.arraycopy(body, 0, result, headerLine.length, body.length);

        return result;
    }
}
