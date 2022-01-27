package http.response;

import http.HttpHeaders;
import http.resource.Resource;

import java.util.Map;

public class HttpResponse {

    private HttpStatusCode httpStatusCode = HttpStatusCode.OK;
    private HttpHeaders httpHeaders = new HttpHeaders();
    private Resource body;

    public HttpResponse() {

    }

    public Map<String, String> getHeaders() {
        return httpHeaders.getHeaders();
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public Resource getHttpResponseBody() {
        return body;
    }

    public void redirect(String redirectUri) {
        httpStatusCode = HttpStatusCode.FOUND;
        httpHeaders.addHeader("Location", redirectUri);
    }

    public void body(Resource resource) {
        httpHeaders.addHeader("Content-Type", resource.getType() + ";charset=utf-8");
        body = resource;
    }

    public void addCookieAttribute(String cookieName, String cookieValue) {
        httpHeaders.addHeader("Set-Cookie", String.format("%s=%s; Path=/", cookieName, cookieValue));
    }
}
