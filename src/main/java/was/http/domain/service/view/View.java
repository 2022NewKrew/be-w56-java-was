package was.http.domain.service.view;

import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.meta.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class View {
    private final boolean isForward;

    private final String path;
    private final HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    public View(HttpStatus status, String path, boolean isForward) {
        this.status = status;
        this.path = path;
        this.isForward = isForward;
        body = new byte[0];
    }

    public View(HttpStatus status) {
        this.status = status;
        this.body = new byte[0];
        this.isForward = false;
        this.path = null;
    }

    public View(HttpStatus status, byte[] body) {
        this.status = status;
        this.body = body;
        this.isForward = false;
        this.path = null;
    }

    public View(HttpStatus status, String path, byte[] body) {
        this.status = status;
        this.body = body;
        this.path = path;
        this.isForward = false;
    }

    public boolean isForward() {
        return isForward;
    }

    public String getPath() {
        return path;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public View addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public View addAllHeader(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void render(HttpRequest req, HttpResponse res) {
        if (res.getStatus() == null) {
            res.setStatus(status);
        }

        res.addAllHeader(headers);

        if (res.getContentLength() == 0) {
            res.setBody(body);
        }
    }
}
