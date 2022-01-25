package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final HttpStatus status;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(HttpStatus status) {
        this(null, null, status);
    }

    public HttpResponse(Map<String, String> headers, HttpStatus status) {
        this(null, headers, status);
    }

    public HttpResponse(byte[] body, HttpStatus status) {
        this(body, null, status);
    }

    public HttpResponse(byte[] body, Map<String, String> headers, HttpStatus status) {
        this.body = body;
        this.headers = Objects.requireNonNullElseGet(headers, HashMap::new);
        this.status = status;
        setContentLengthHeader();
    }

    private void setContentLengthHeader() {
        if (body == null || headers.containsKey("Content-Length")) {
            return;
        }
        headers.put("Content-Length", String.valueOf(body.length));
    }

    public void send(DataOutputStream dos) throws IOException {
        dos.writeBytes(getStatusLine());
        dos.writeBytes(getResponseHeader());
        dos.writeBytes("\r\n");
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    private String getStatusLine() {
        return String.format("HTTP/1.1 %d %s\r\n", status.getStatusCode(), status);
    }

    private String getResponseHeader() {
        if (headers == null) {
            return "";
        }
        StringBuilder headerString = new StringBuilder();
        for (String key : headers.keySet()) {
            headerString.append(key)
                    .append(": ")
                    .append(headers.get(key))
                    .append("\r\n");
        }
        return headerString.toString();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpResponse)) {
            return false;
        }
        HttpResponse other = (HttpResponse) obj;
        return status.equals(other.status)
                && headers.equals(other.headers)
                && Arrays.equals(body, other.body);
    }
}
