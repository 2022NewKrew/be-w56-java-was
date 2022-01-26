package http.response;

import http.HttpStatus;

public class HttpResponse {
    private final HttpResponseStatusLine statusLine;
    private final HttpResponseHeaders headers;
    private final HttpResponseBody body;

    public HttpResponse(HttpResponseStatusLine statusLine, HttpResponseHeaders headers, HttpResponseBody body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpResponseStatusLine getStatusLine() {
        return statusLine;
    }

    public HttpResponseHeaders getHeaders() {
        return headers;
    }

    public HttpResponseBody getBody() {
        return body;
    }

    public HttpStatus getStatus(){
        return getStatusLine().getStatus();
    }

    public String getStatusString(){
        return statusLine.toString();
    }

    public int getBodyLength(){
        return body.getBodyLength();
    }

    public String getRedirectHeader(){
        return headers.getHeaderByKey("Location");
    }
}
