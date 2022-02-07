package controller;

import db.DataBase;
import java.util.Map;
import model.User;
import model.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import request.RequestBody;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;
import response.ResHeader;
import response.ResponseHeader;
import response.ResponseHeader.ResponseHeaderBuilder;
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
                ResponseHeader responseHeader = new ResponseHeaderBuilder()
                        .set(ResHeader.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(responseHeader)
                        .setBody(body)
                        .build();
            }
        );

        UrlMapper.put(
            "/user/create",
            "POST",
            (HttpRequest httpRequest) -> {
                RequestBody requestBody = httpRequest.getRequestBody();
                String body = new String(requestBody.getBody());
                body = UrlUtils.decode(body);
                Map<String, String> data = IOUtils.getBodyData(body);

                try {
                    User user = new User(data.get("userId"), data.get("password"),
                            data.get("name"), data.get("email"));
                    DataBase.addUser(user);

                    LOG.info("SignIn Succeeded - {}", user);
                    ResponseHeader responseHeader = new ResponseHeaderBuilder()
                            .set(ResHeader.LOCATION, "/")
                            .build();

                    return new HttpResponseBuilder(HttpStatusCode.FOUND)
                            .setHeader(responseHeader)
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
                ResponseHeader responseHeader = new ResponseHeaderBuilder()
                        .set(ResHeader.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(responseHeader)
                        .setBody(body)
                        .build();
            }
        );

        UrlMapper.put(
            "/user/login",
            "POST",
            (HttpRequest httpRequest) -> {
                RequestBody requestBody = httpRequest.getRequestBody();
                String body = new String(requestBody.getBody());
                body = UrlUtils.decode(body);
                Map<String, String> data = IOUtils.getBodyData(body);

                try {
                    UserLogin user = new UserLogin(data.get("userId"), data.get("password"));
                    boolean result = DataBase.login(user);
                    String cookieValue = result ? "logined=true; Path=/" : "logined=false";

                    LOG.info(result ? "Login Failed" : "Login Succeeded - {}", user);
                    ResponseHeader responseHeader = new ResponseHeaderBuilder()
                            .set(ResHeader.SET_COOKIE, cookieValue)
                            .set(ResHeader.LOCATION, "/")
                            .build();

                    return new HttpResponseBuilder(HttpStatusCode.FOUND)
                            .setHeader(responseHeader)
                            .build();

                } catch (NullPointerException | IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                    return new HttpResponseBuilder(HttpStatusCode.NOT_ACCEPTABLE).build();
                }
            }
        );
    }
}
