package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseMaker;
import util.Pair;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestHeaders;
import web.http.request.HttpRequestLine;
import web.http.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewService {
    private static final Logger log = LoggerFactory.getLogger(ViewService.class);

    public static HttpResponse others(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Content-type", httpRequest.getHttpRequestHeaders().getHeaderFirstValueByKey("Accept")));

        HttpResponseBody body = new HttpResponseBody(Files.readAllBytes(new File("./webapp" + requestLine.getUrl()).toPath()));

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse loginPage(HttpRequest httpRequest) throws IOException {
        HttpRequestHeaders requestHeaders = httpRequest.getHttpRequestHeaders();

        if (requestHeaders.isHeader("Cookie") && checkCookieLogin(requestHeaders)){
            return HttpResponseMaker.redirectIndexPage(httpRequest);
        }

        return others(httpRequest);
    }

    public static HttpResponse userListPage(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        HttpRequestHeaders requestHeaders = httpRequest.getHttpRequestHeaders();

        if (!requestHeaders.isHeader("Cookie") || !checkCookieLogin(requestHeaders)){
            return HttpResponseMaker.redirectLoginPage(httpRequest);
        }

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.OK);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody(getUserListBody());

        return new HttpResponse(statusLine, headers, body);
    }

    private static boolean checkCookieLogin(HttpRequestHeaders headers){
        String cookieValue = headers.getHeaderValueByKey("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        return cookies.get("logined").equals("true");
    }

    private static byte[] getUserListBody() throws IOException {
        String baseHtml = new String(Files.readAllBytes(new File("./webapp/user/list.html").toPath()));
        String changeHtml = baseHtml.replace("{{userList}}", getUserListHtml());
        return changeHtml.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUserListHtml(){
        List<User> users = new ArrayList<>(DataBase.findAll());

        AtomicInteger atomicInteger = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        users.forEach(user -> {
            sb.append("<tr>\n").append("<th scope=\"row\">").append(atomicInteger.incrementAndGet()).append("</th>\n");
            sb.append("<td>").append(user.getUserId()).append("</td>\n");
            sb.append("<td>").append(user.getName()).append("</td>\n");
            sb.append("<td>").append(user.getEmail()).append("</td>\n");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n").append("</tr>\n");
        });

        return sb.toString();
    }
}
