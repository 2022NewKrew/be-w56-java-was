package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import util.HttpRequestUtils;
import util.HttpResponseMaker;
import util.Pair;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestHeaders;
import web.http.request.HttpRequestLine;
import web.http.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ViewService {
    private static final Logger log = LoggerFactory.getLogger(ViewService.class);

    public static HttpResponse others(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Content-type", httpRequest.getHeaders().getHeaderFirstValueByKey("Accept")));

        HttpResponseBody body = new HttpResponseBody(Files.readAllBytes(new File("./webapp" + requestLine.getUrl()).toPath()));

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse loginPage(HttpRequest httpRequest) throws IOException {
        HttpRequestHeaders requestHeaders = httpRequest.getHeaders();

        if (requestHeaders.isHeader("Cookie") && checkCookieLogin(requestHeaders)){
            return HttpResponseMaker.redirectIndexPage(httpRequest);
        }

        return others(httpRequest);
    }

    public static HttpResponse userListPage(HttpRequest httpRequest, byte[] userListBody) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        HttpRequestHeaders requestHeaders = httpRequest.getHeaders();

        if (!requestHeaders.isHeader("Cookie") || !checkCookieLogin(requestHeaders)){
            return HttpResponseMaker.redirectLoginPage(httpRequest);
        }

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody(userListBody);

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse indexPage(HttpRequest httpRequest, byte[] postListBody) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody(postListBody);

        return new HttpResponse(statusLine, headers, body);
    }


    private static boolean checkCookieLogin(HttpRequestHeaders headers){
        String cookieValue = headers.getHeaderValueByKey("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        return cookies.get("logined").equals("true");
    }
}
