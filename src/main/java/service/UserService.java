package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import util.HttpRequestUtils;
import util.HttpResponseMaker;
import util.Pair;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestLine;
import web.http.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        UserRepository.addUser(user);
    }

    public static HttpResponse signUp(HttpRequest httpRequest) {
        String requestBody = httpRequest.getBodyData();
        addUser(requestBody);

        return HttpResponseMaker.redirectIndexPage(httpRequest);
    }

    public static HttpResponse login(HttpRequest httpRequest) {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        String requestBody = httpRequest.getBodyData();

        HttpResponseStatusLine statusLine = new HttpResponseStatusLine(requestLine.getVersion(), HttpStatus.REDIRECT);
        HttpResponseHeaders headers = new HttpResponseHeaders();
        HttpResponseBody body = new HttpResponseBody();

        if(canUserLogin(requestBody)){
            headers.addHeader(new Pair("Set-Cookie", "logined=true; Path=/"));
            headers.addHeader(new Pair("Location", "http://localhost:8080/index.html"));
            return new HttpResponse(statusLine, headers, body);
        }

        headers.addHeader(new Pair("Set-Cookie", "logined=false; Path=/"));
        headers.addHeader(new Pair("Location", "http://localhost:8080/user/login_failed.html"));

        return new HttpResponse(statusLine, headers, body);
    }

    private static boolean canUserLogin(String body){
        Map<String, String> userInfo = HttpRequestUtils.parseBody(body);
        User user = UserRepository.findUserById(userInfo.get("userId"));
        return user.getPassword().equals(userInfo.get("password"));
    }

    public static byte[] getUserListBody() throws IOException {
        String baseHtml = new String(Files.readAllBytes(new File("./webapp/user/list.html").toPath()));
        String changeHtml = baseHtml.replace("{{userList}}", getUserListHtml());
        return changeHtml.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUserListHtml(){
        List<User> users = UserRepository.findAll();

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
