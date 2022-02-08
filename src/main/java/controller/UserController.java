package controller;

import db.DataBase;
import java.util.Map;
import model.User;
import model.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpStatusCode;
import http.response.HttpResponseHeaderEnum;
import http.response.HttpResponseHeader;
import http.response.HttpResponseHeader.ResponseHeaderBuilder;
import util.IOUtils;
import util.UrlUtils;
import webserver.UrlMapper;

public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserController() {}

    public static void register() {

        UrlMapper.put(
            "/user/form.html",
            "GET",
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/user/form.html");
                HttpResponseHeader httpResponseHeader = new ResponseHeaderBuilder()
                        .set(HttpResponseHeaderEnum.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(httpResponseHeader)
                        .setBody(body)
                        .build();
            }
        );

        UrlMapper.put(
            "/user/create",
            "POST",
            (HttpRequest httpRequest) -> {
                HttpRequestBody httpRequestBody = httpRequest.getRequestBody();
                String body = String.valueOf(httpRequestBody.getBody());
                body = UrlUtils.decode(body);
                Map<String, String> data = IOUtils.getBodyData(body);

                try {
                    User user = new User(data.get("userId"), data.get("password"),
                            data.get("name"), data.get("email"));
                    DataBase.addUser(user);

                    LOG.info("SignIn Succeeded - {}", user);
                    HttpResponseHeader httpResponseHeader = new ResponseHeaderBuilder()
                            .set(HttpResponseHeaderEnum.LOCATION, "/")
                            .build();

                    return new HttpResponseBuilder(HttpStatusCode.FOUND)
                            .setHeader(httpResponseHeader)
                            .build();

                } catch (NullPointerException | IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                    return new HttpResponseBuilder(HttpStatusCode.NOT_ACCEPTABLE).build();
                }
            }
        );

        UrlMapper.put(
            "/user/login.html",
            "GET",
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/user/login.html");
                HttpResponseHeader httpResponseHeader = new ResponseHeaderBuilder()
                        .set(HttpResponseHeaderEnum.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(httpResponseHeader)
                        .setBody(body)
                        .build();
            }
        );

        UrlMapper.put(
            "/user/login",
            "POST",
            (HttpRequest httpRequest) -> {
                HttpRequestBody httpRequestBody = httpRequest.getRequestBody();
                String body = String.valueOf(httpRequestBody.getBody());
                body = UrlUtils.decode(body);
                Map<String, String> data = IOUtils.getBodyData(body);

                try {
                    UserLogin user = new UserLogin(data.get("userId"), data.get("password"));
                    boolean result = DataBase.login(user);
                    String cookieValue = result ? "logined=true; Path=/" : "logined=false";

                    LOG.info(result ? "Login Failed" : "Login Succeeded - {}", user);
                    HttpResponseHeader httpResponseHeader = new ResponseHeaderBuilder()
                            .set(HttpResponseHeaderEnum.SET_COOKIE, cookieValue)
                            .set(HttpResponseHeaderEnum.LOCATION, "/")
                            .build();

                    return new HttpResponseBuilder(HttpStatusCode.FOUND)
                            .setHeader(httpResponseHeader)
                            .build();

                } catch (NullPointerException | IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                    return new HttpResponseBuilder(HttpStatusCode.NOT_ACCEPTABLE).build();
                }
            }
        );
    }
}
