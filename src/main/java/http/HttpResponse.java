package http;

import com.google.common.collect.Maps;
import java.util.Map;

public class HttpResponse {

    private final HttpVersion version;
    private final Map<HttpHeader, String> headers;
    private HttpStatus status;
    private String path;
    private byte[] body;

    public HttpResponse() {
        this.version = HttpVersion.HTTP_1_1;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.headers = Maps.newTreeMap();
    }

    public void putHeader(HttpHeader header, String value) {
        headers.put(header, value);
    }

    public void writeBody(byte[] body) {
        this.body = body;
        putHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length));
    }

    public void ok(String path) {
        status = HttpStatus.OK;
        this.path = path;
        putHeader(HttpHeader.CONTENT_TYPE, MediaType.fromExtension(extractExtension(path)));
    }

    public void sendRedirect(String path) {
        status = HttpStatus.FOUND;
        this.path = path;
        putHeader(HttpHeader.CONTENT_TYPE, MediaType.fromExtension(extractExtension(path)));
        putHeader(HttpHeader.LOCATION, path);
    }

    private String extractExtension(String path) {
        String[] tokens = path.split("\\.");
        return tokens[tokens.length - 1];
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<HttpHeader, String> getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBody() {
        return body;
    }
}
