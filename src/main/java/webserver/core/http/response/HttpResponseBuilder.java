package webserver.core.http.response;

import webserver.core.http.request.Header;

public class HttpResponseBuilder {
    private HttpStatus status;
    private Header header = new Header();
    private byte[] body = new byte[0];

    public HttpResponseBuilder(){
        //set default
        this.header.addHeaderValue("Content-Type", "text/html; charset=utf-8");
    }

    public HttpResponseBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponseBuilder setHeader(Header header) {
        this.header = header;
        return this;
    }

    public HttpResponseBuilder addHeaderValue(String key, String value){
        this.header.addHeaderValue(key, value);
        return this;
    }

    public HttpResponseBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(status, header, body);
    }

}
