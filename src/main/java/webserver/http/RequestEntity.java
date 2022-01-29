package webserver.http;

import http.HttpHeaders;
import webserver.http.request.RequestBody;

import java.util.List;

public class RequestEntity<T extends RequestBody<?>> {
    private HttpHeaders headers;
    private RequestParams requestParams;
    private List<Cookie> cookies;
    private T requestBody;

    public RequestEntity(HttpHeaders headers, RequestParams requestParams, List<Cookie> cookies, T requestBody) {
        this.headers = headers;
        this.requestParams = requestParams;
        this.cookies = cookies;
        this.requestBody = requestBody;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public RequestParams getRequestParams() {
        return requestParams;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public T getRequestBody() {
        return requestBody;
    }
}
