package web.controller;

import web.http.response.HttpStatus;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestLine;
import web.http.response.HttpResponse;
import web.http.response.HttpResponseBody;
import web.http.response.HttpResponseHeaders;
import web.http.response.HttpResponseStatusLine;
import service.UserService;
import util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RequestController {

    public static HttpResponse getResponse(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        String url = requestLine.getUrl();
        String method = requestLine.getMethod();

        HttpRequestLineInfo requestLineInfo = HttpRequestLineInfo.lookup(url, method);
        return handleRequest(requestLineInfo, httpRequest);
    }

    public static HttpResponse handleRequest(HttpRequestLineInfo requestLineInfo, HttpRequest httpRequest) throws IOException {
        switch (requestLineInfo){
            case SIGN_UP:
                return signUp(httpRequest);
            default:
                return others(httpRequest);
        }
    }

    public static HttpResponse signUp(HttpRequest httpRequest) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        String requestBody = httpRequest.getBodyData();

        UserService.addUser(requestBody);

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));

        HttpResponseBody body = new HttpResponseBody();

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse others(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody(Files.readAllBytes(new File("./webapp" + requestLine.getUrl()).toPath()));

        return new HttpResponse(statusLine, headers, body);
    }
}
