package http.impl;

import http.HttpHeaders;
import http.HttpResponse;
import http.StatusCode;

class HttpResponseImpl implements HttpResponse {

    private byte[] responseBody;

    public HttpResponseImpl(byte[] body) {
        this.responseBody = body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }

    @Override
    public byte[] getResponseBody() {
        return responseBody;
    }

    @Override
    public StatusCode getStatusCode() {
        return null;
    }
}
