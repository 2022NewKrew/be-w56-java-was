package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.Pair;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestHeaders;
import web.http.request.HttpRequestLine;
import web.http.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static void addUser(String body){
        Map<String, String> queries = HttpRequestUtils.parseBody(body);
        String userId = queries.get("userId");
        String name = queries.get("name");
        String password = queries.get("password");
        String email = queries.get("email");

        User user = new User(userId, password, name, email);
        log.info("Insert User : {} ", user);
        DataBase.addUser(user);
    }

    public static HttpResponse signUp(HttpRequest httpRequest) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        String requestBody = httpRequest.getBodyData();

        addUser(requestBody);

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));

        HttpResponseBody body = new HttpResponseBody();

        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse login(HttpRequest httpRequest) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();

        headers.addHeader(new Pair("Set-Cookie", "logined=true; Path=/"));
        headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));

        HttpResponseBody body = new HttpResponseBody();

        return new HttpResponse(statusLine, headers, body);
    }
}
