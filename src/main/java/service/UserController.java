package service;

import db.DataBase;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestHeader;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import http.response.HttpResponseStatusLine;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *  process 메소드를 통해 HttpRequest 를 넘겨주면
 *  필요한 작업 후 HttpResponse 를 반환
 */
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController() {
    }

    public HttpResponse process(HttpRequest request) throws IOException {
        final HttpRequestLine requestLine = request.line();
        String[] urlTokens = requestLine.url().split("/");
        String url = urlTokens[urlTokens.length - 1].split("\\?")[0];

        if (requestLine.method().equals("GET") && url.equals("create")) {
            log.debug("GET /create called");
            return createUser(requestLine);
        } else { // TODO : 임시 구현
            log.debug("{} {}", requestLine.method(), requestLine.url());
            return readStaticFile(requestLine.url());
        }
    }

    private HttpResponse readStaticFile(String url) throws IOException {
        HttpResponseBody responseBody = HttpResponseBody.createFromUrl(url);
        HttpResponseHeader responseHeader = new HttpResponseHeader(url, HttpStatus.OK, responseBody.length());

        return new HttpResponse(responseHeader, responseBody);
    }

    private HttpResponse createUser(HttpRequestLine requestLine) {
        Map<String, String> queryString = requestLine.queryString();
        System.out.println(queryString);
        User newUser = new User(
                queryString.get("userId"),
                queryString.get("password"),
                queryString.get("name"),
                queryString.get("email"));

        DataBase.addUser(newUser);
        return redirect("/index.html");
    }

    private HttpResponse redirect(String redirectUrl) {
        HttpResponseHeader responseHeader = new HttpResponseHeader(redirectUrl, HttpStatus.FOUND, 0);
        responseHeader.addKeyValue("Location", redirectUrl);
        byte[] emptyBody = "".getBytes(StandardCharsets.UTF_8);
        HttpResponseBody responseBody = new HttpResponseBody(emptyBody);

        return new HttpResponse(responseHeader, responseBody);
    }
}
