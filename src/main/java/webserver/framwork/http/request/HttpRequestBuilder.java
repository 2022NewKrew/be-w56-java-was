package webserver.framwork.http.request;

import webserver.framwork.http.Header;

import java.util.Map;

public class HttpRequestBuilder {

    private String url;
    private HttpMethod method;
    private Header header;
    private Map<String, String> params;
    private Map<String, String> body;

    public HttpRequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpRequestBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder setHeader(Header header){
        this.header = header;
        return this;
    }

    public HttpRequestBuilder setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public HttpRequestBuilder setBody(Map<String, String> body) {
        this.body = body;
        return this;
    }

    public HttpRequest build(){
        return new HttpRequest(url, method, header, params, body);
    }
}
