package util;

import model.User;
import network.HttpMethod;
import network.HttpRequest;
import network.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);

    public static HttpResponse requestMapping(HttpRequest httpRequest) throws IOException {
        HttpMethod httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (httpMethod.equals(HttpMethod.GET)) {
            switch (path) {
                case "/" : return indexView(httpRequest);
                case "/user/signup" : return signupView(httpRequest);
            }
        }

        if (httpMethod.equals(HttpMethod.POST)) {
            switch (path) {
                case "/user/create" : return signup(httpRequest);
            }
        }

        return defaultPath(httpRequest);
    }

    private static HttpResponse indexView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }

    private static HttpResponse signupView(HttpRequest httpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/form.html").toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }

    private static HttpResponse signup(HttpRequest httpRequest) throws IOException {
        String requestBody = httpRequest.getBody();
        Map<String, String> data = HttpRequestUtils.parseQueryString(requestBody);

        String userId = URLDecoder.decode(data.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(data.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(data.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(data.get("email"), StandardCharsets.UTF_8);
        User user = new User(userId, password, name, email);
        log.debug(user.toString());

        List<String> headers = HttpResponseUtils.response302(httpRequest, "/");
        return new HttpResponse(headers);
    }

    private static HttpResponse defaultPath(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }
}
