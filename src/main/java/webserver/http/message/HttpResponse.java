package webserver.http.message;

import webserver.http.message.values.HttpContentType;
import webserver.http.message.values.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final HttpRequest request;
    private HttpResponseStatus status;
    private byte[] responseBody;
    private HttpContentType contentType;
    private String cookie;
    private Map<String, Object> model;

    public HttpResponse(HttpRequest request) {
        this.request = request;
        this.model = new HashMap<>();
    }

    public HttpRequest getRequest() {
        return request;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public HttpContentType getContentType() {
        return contentType;
    }

    public String getCookie() {
        return cookie;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public void setContentType(HttpContentType contentType) {
        this.contentType = contentType;
    }

    public void setCookieDefault() {
        this.cookie = "logined=true; Path=/";
    }
}
