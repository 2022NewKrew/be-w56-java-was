package http.response;

import http.resource.Resource;

import java.util.Map;

public class HttpResponse {

    private HttpStatusCode httpStatusCode = HttpStatusCode.OK;
    private HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
    private Resource body;

    public HttpResponse() {

    }

    public Map<String, String> getHeaders() {
        return httpResponseHeader.getHeaders();
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public Resource getHttpResponseBody() {
        return body;
    }

    public void redirect(String redirectUri) {
        httpStatusCode = HttpStatusCode.FOUND;
        httpResponseHeader.addHeader("Location", redirectUri);
    }

    public void body(Resource resource) {
        httpResponseHeader.addHeader("Content-Type", resource.getType() + ";charset=utf-8");
        body = resource;
    }
}
