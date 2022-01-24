package http.impl;

import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpResponseRenderer;

public class HttpFactory {

    private static final HttpRequestParser httpRequestParser = new HttpRequestParserImpl();
    private static final HttpResponseRenderer httpResponseRenderer = new HttpResponseRendererImpl();

    public static HttpRequestParser getHttpRequestParser() {
        return httpRequestParser;
    }

    public static HttpResponseRenderer getHttpResponseRenderer() {
        return httpResponseRenderer;
    }

    public static HttpResponse createHttpResponse(byte[] body) {
        return new HttpResponseImpl(body);
    }
}
