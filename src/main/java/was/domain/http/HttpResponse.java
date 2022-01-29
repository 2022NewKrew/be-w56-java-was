package was.domain.http;

import was.meta.HttpHeaders;
import was.meta.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final String version;
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    private String viewPath = null;

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public boolean hasNotViewPath() {
        return Objects.isNull(viewPath);
    }

    public String getViewPath() {
        return viewPath;
    }

    public static HttpResponse of(HttpRequest httpRequest) {
        return new HttpResponse(httpRequest.getVersion(), null, null);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void initHeaders() {
        headers.clear();
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
