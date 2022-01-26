package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeaders;
import http.response.HttpResponseStatusLine;
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
        return handleRequest(requestLineInfo, requestLine);
    }

    public static HttpResponse handleRequest(HttpRequestLineInfo requestLineInfo, HttpRequestLine requestLine) throws IOException {
        switch (requestLineInfo){
            case SIGN_UP:
                return signUp(requestLine);
            default:
                return others(requestLine);
        }
    }

    public static HttpResponse signUp(HttpRequestLine httpRequestLine) {
        UserService.addUser(httpRequestLine.getUrl());

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(httpRequestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));

        HttpResponseBody body = new HttpResponseBody();

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse others(HttpRequestLine httpRequestLine) throws IOException {
        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(httpRequestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody(Files.readAllBytes(new File("./webapp" + httpRequestLine.getUrl()).toPath()));

        return new HttpResponse(statusLine, headers, body);
    }
}
