package http.response;

import http.header.HttpHeaders;
import http.status.HttpStatus;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private HttpStatus status;
    private HttpHeaders headers;
    private byte[] body;

    public HttpResponse() {
        this.status = HttpStatus.OK;
        this.headers = new HttpHeaders();
        this.body = new byte[0];
    }

    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    public void status(HttpStatus status) {
        this.status = status;
    }

    public void body(byte[] body) {
        this.body = body;
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length));
    }

    public byte[] getStatus() {
        String responseLine = "HTTP/1.1 " + status.getStatusCode() + " " + status.getStatusMessage() + "\r\n";
        return responseLine.getBytes();
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getHeaders() {
        StringBuilder sb = new StringBuilder();
        for (String key : headers.keySet()) {
            String value = headers.getFirst(key);
            sb.append(key + ": " + value + "\r\n");
        }
        sb.append("\r\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
