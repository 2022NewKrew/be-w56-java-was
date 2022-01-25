package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String version;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private byte[] body;

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    public void set200Header() {
        this.version = "HTTP/1.1";
        this.statusCode = 200;
        this.statusMessage = "OK";
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

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String toHeader() {
        String responseMessage = String.format("%s %d %s \r\n", version, statusCode, statusMessage);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseMessage += String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
        }
        responseMessage += "\r\n";
        return responseMessage;
    }
}
