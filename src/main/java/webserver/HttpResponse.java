package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String version;
    private HttpStatus httpStatus;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private byte[] body;

    public HttpResponse() {
        this.version = "HTTP/1.1";
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
    }

    public void addCookie(String key, String value) {
        cookies.put(key, value);
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

    public Map<String, String> getCookies() {
        return cookies;
    }

    public byte[] getBody() {
        if (body == null) {
            body = new byte[0];
        }
        return body;
    }

    public String toHeader() {
        String line = String.format("%s %d %s \r\n", version, httpStatus.getCode(), httpStatus.getMessage());
        String header = "";

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            header += String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
        }
        String cookie = "";
        if (!cookies.isEmpty()) {
            cookie = "Set-Cookie: ";
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookie += String.format("%s=%s; ", entry.getKey(), entry.getValue());
            }
            cookie += "\r\n";
        }
        return line + header + cookie + "\r\n";
    }
}
