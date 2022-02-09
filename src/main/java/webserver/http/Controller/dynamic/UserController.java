package webserver.http.Controller.dynamic;

import dto.UserSignUpDto;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.HttpRequestUtils;
import webserver.http.Controller.HttpController;
import webserver.http.request.HttpRequest;
import webserver.http.response.ContentType;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;
import webserver.http.service.UserService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static util.HttpRequestUtils.urlToFile;

public class UserController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService = new UserService();

    @Override
    public boolean isValidRequest(HttpRequest request) {
        return request.getUrl().contains("user/create") || request.getUrl().contains("user/list");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        if (request.getUrl().contains("user/create")) {
            switch (request.getMethod()) {
                case GET:
                    String queries = request.getUrl().split(Constants.QUESTION)[1];
                    Map<String, String> queriesMap = HttpRequestUtils.parseQueryString(queries);
                    userService.join(new UserSignUpDto(queriesMap.get("userId"), queriesMap.get("password"), queriesMap.get("name"), queriesMap.get("email")));
                    log.debug("UserController handleRequest User joined by GET");
                    log.debug("UserController handleRequest GET queries : {} ", queries);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setRedirect("/index.html")
                            .build();
                case POST:
                    Map<String, String> queriesPost = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
                    userService.join(new UserSignUpDto(queriesPost.get("userId"), queriesPost.get("password"), queriesPost.get("name"), queriesPost.get("email")));
                    log.debug("UserController handleRequest User joined by POST");
                    log.debug("UserController handleRequest POST queries : {} ", queriesPost);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setRedirect("/index.html")
                            .build();
                default:
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._404)
                            .setRedirect("/index.html")
                            .build();
            }
        } else if (request.getUrl().contains("user/list")) {
            switch (request.getMethod()) {
                case GET:
                    log.debug("UserController handleRequest list by GET");
                    String cookies = request.getHttpRequestHeader().getHeaders().get("Cookie");
                    Map<String, String> parsedCookies = HttpRequestUtils.parseCookies(cookies);
                    if (Objects.equals(parsedCookies.get(Constants.HTTP_COOKIE_LOGINED_KEY), "true")) {
                        Collection<User> users = userService.findUsers();
                        StringBuilder sb = new StringBuilder();
                        int index = 3;
                        for (User user : users) {
                            sb.append("<tr>");
                            sb.append("<th scope=\"row\">" + index + "</th> <td>" + user.getUserId() + "</td> <td> " + user.getName() + "</td> <td> " + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
                            sb.append("</tr>");
                            index++;
                        }

                        Path target = urlToFile(request.getUrl());
                        String[] tokens = target.toString().split(Constants.DOT);
                        ContentType contentType = ContentType.of(tokens[tokens.length - 1].toUpperCase());
                        File file = target.toFile();
                        String fileData = new String(Files.readAllBytes(file.toPath()));
                        fileData = fileData.replace("%user_list%", URLDecoder.decode(sb.toString(), StandardCharsets.UTF_8));
                        byte[] body = fileData.getBytes(StandardCharsets.UTF_8);
                        log.debug("UserController handle request ContentType : {}, ContentLength : {}", contentType.getExtension(), body.length);
                        return new HttpResponse.Builder(out)
                                .setBody(body)
                                .setHttpStatus(HttpStatus._200)
                                .setContentType(contentType.getExtension())
                                .setContentLength(body.length)
                                .setRedirect("./webapp/index.html")
                                .build();

                    } else {
                        return new HttpResponse.Builder(out)
                                .setHttpStatus(HttpStatus._302)
                                .setRedirect("/user/login.html")
                                .build();
                    }
            }
        }
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._404)
                .setRedirect("/index.html")
                .build();
    }
}

