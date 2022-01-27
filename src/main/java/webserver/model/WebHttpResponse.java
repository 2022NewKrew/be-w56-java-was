package webserver.model;

import java.io.IOException;
import java.util.*;

public class WebHttpResponse {
    private String version;
    private HttpStatus httpStatus;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private byte[] body;

    private WebHttpResponse(WebHttpRequest httpRequest) {
        if (httpRequest.version().isPresent())
            switch (httpRequest.version().get()) {
                case HTTP_1_1:
                    version = "HTTP/1.1";
                    break;
                case HTTP_2:
                    version = "HTTP/2.0";
                    break;
            }
        else
            version = "HTTP/1.1";

        headers = new HashMap<>();
        this.cookies = new ArrayList<>();
    }

    public static WebHttpResponse of(WebHttpRequest httpRequest) throws IOException {
        return new WebHttpResponse(httpRequest);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = new byte[body.length];
        System.arraycopy(body, 0, this.body, 0, body.length);
    }

    public byte[] getBody() {
        return body;
    }

    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookies.add(cookie);
    }

    @Override
    public String toString() {
        String responseHeader = version + " " + httpStatus + "\r\n";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            responseHeader += key + ": " + value + "\r\n";
        }
        for (Cookie cookie : cookies) {
            responseHeader += cookie.toString() + "\r\n";
        }
        return responseHeader + "\r\n";
    }
}
