package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private String httpVersion;
    private String statusCode;
    private Map<String, String> headers;
    private byte[] body;
    private DataOutputStream dos;

    public HttpResponse(OutputStream out, HttpRequest request) throws IOException {
        dos = new DataOutputStream(out);
        headers = new HashMap<>();
        init(request);
    }

    private void init(HttpRequest request) {
        httpVersion = request.getProtocol();
    }

    public void initBody(String url) throws IOException {
        body = HttpRequestUtils.getBodyData(url);
        statusCode = HttpRequestUtils.getStatusCode(url);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
        log.info("key : {}, value : {}, headers : {}", key, value, headers);
    }

    public void send(String path) throws IOException {
        initBody(path);
        HttpResponseUtils.responseHeader(dos, this);
        HttpResponseUtils.responseBody(dos, body);
    }

    public void redirect(String path) {
        headers.put("Location", path);
        statusCode = "302";
        HttpResponseUtils.responseHeader(dos, this);
    }


    public String getHttpVersion() {
        return httpVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getTotalHeader() {
        StringBuilder header = new StringBuilder();
        for (String key : headers.keySet()) {
            header.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        return header.toString();
    }
}
