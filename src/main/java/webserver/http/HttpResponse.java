package webserver.http;

import webserver.WebServerConfig;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpHeader trailingHeaders;
    private HttpResponseStatus status;
    private byte[] body;

    public HttpResponse() {
        this(WebServerConfig.RESPONSE_HTTP_VERSION);
    }

    public HttpResponse(HttpVersion version) {
        this.version = version;
        trailingHeaders = new HttpHeader();
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpHeader headers() {
        return trailingHeaders;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public HttpResponse setStatus(HttpResponseStatus status) {
        this.status = status;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpResponse setBody(byte[] contents) {
        this.body = contents;
        return this;
    }


}
