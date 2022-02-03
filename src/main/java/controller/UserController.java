package controller;

import ch.qos.logback.core.util.FileUtil;
import db.DataBase;
import model.User;
import network.HttpRequest;
import network.HttpResponse;
import org.h2.store.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DataBaseUtils;
import util.HttpRequestUtils;
import util.HttpResponseUtils;

import javax.swing.text.html.HTML;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public static HttpResponse indexView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }

    public static HttpResponse signupView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/form.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }

    public static HttpResponse signup(HttpRequest httpRequest) {
        String requestBody = httpRequest.getBody();
        Map<String, String> data = HttpRequestUtils.parseQueryString(requestBody);

        String userId = URLDecoder.decode(data.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(data.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(data.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(data.get("email"), StandardCharsets.UTF_8);
        User user = new User(userId, password, name, email);

        DataBase.addUser(user);

        List<String> headers = HttpResponseUtils.response302(httpRequest, "/");
        return new HttpResponse(headers);
    }

    public static HttpResponse loginView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/login.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
        }

    public static HttpResponse loginFailedView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }

    public static HttpResponse login(HttpRequest httpRequest) {
        String requestBody = httpRequest.getBody();
        Map<String, String> data = HttpRequestUtils.parseQueryString(requestBody);

        String userId = URLDecoder.decode(data.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(data.get("password"), StandardCharsets.UTF_8);

        User loginUser = DataBase.findUserById(userId);
        boolean logined = true;
        String location = "/";
        if (loginUser == null || !loginUser.getPassword().equals(password)) {
            logined = false;
            location = "/user/login_failed";
        }

        List<String> headers = HttpResponseUtils.response302(httpRequest, location);
        headers.add(1, String.format("Set-Cookie: logined=%b; Path=/\r\n", logined));
        return new HttpResponse(headers);
    }

    public static HttpResponse userListView(HttpRequest httpRequest) throws IOException {
        Path file = new File("./webapp/user/list.html").toPath();
        StringBuilder html = new StringBuilder(Files.readString(file));
        String users = DataBaseUtils.setUserTable();

        String tag = "{{#users}}";
        html.replace(html.indexOf(tag), html.indexOf(tag) + tag.length(), users);
        byte[] body = html.toString().getBytes();
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }
}
