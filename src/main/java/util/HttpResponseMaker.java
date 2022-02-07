package util;

import web.http.request.HttpRequest;
import web.http.request.HttpRequestLine;
import web.http.response.*;

public class HttpResponseMaker {

    public static HttpResponse redirectLoginPage(HttpRequest httpRequest){
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Location", "http://localhost:8080/user/login.html"));

        HttpResponseBody body = new HttpResponseBody();
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse redirectIndexPage(HttpRequest httpRequest){
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));

        HttpResponseBody body = new HttpResponseBody();
        return new HttpResponse(statusLine, headers, body);
    }
}
