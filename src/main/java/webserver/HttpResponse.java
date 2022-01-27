package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String version;
    private HttpStatus httpStatus;
    private Map<String, String> headers;
    private byte[] body;

    public HttpResponse() {
        this.headers = new HashMap<>();
        this.version = "HTTP/1.1";
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        setHeader("Content-Length", String.valueOf(body.length));
        this.body = body;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatus getHttpStatus() { return httpStatus; }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        if (body == null) {
            body = new byte[0];
        }
        return body;
    }

    public String toHeader() {
        String responseMessage = String.format("%s %d %s \r\n", version, httpStatus.getCode(), httpStatus.getMessage());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseMessage += String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
        }
        responseMessage += "\r\n";
        return responseMessage;
    }
}
