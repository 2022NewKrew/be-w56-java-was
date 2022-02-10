package webserver.http.message;

import webserver.http.message.values.HttpContentType;
import webserver.http.message.values.HttpResponseStatus;

public class HttpResponse {

    private final HttpRequest request;
    private HttpResponseStatus status;
    private byte[] responseBody;
    private HttpContentType contentType;

    public HttpResponse(HttpRequest request) {
        this.request = request;
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

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public void setContentType(HttpContentType contentType) {
        this.contentType = contentType;
    }
}
