package util;

import model.User;
import network.HttpMethod;
import network.HttpRequest;
import network.HttpStatus;
import network.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);

    public static ResponseBody requestMapping(HttpRequest httpRequest) throws IOException {
        HttpMethod httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (httpMethod.equals(HttpMethod.GET)) {
            switch (path) {
                case "/" : return indexView();
                case "/user/signup" : return signupView();
            }
        }

        if (httpMethod.equals(HttpMethod.POST)) {
            switch (path) {
                case "/user/create" : return signup(httpRequest);
            }
        }

        return defaultPath(path);
    }

    private static ResponseBody indexView() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        return new ResponseBody(HttpStatus.OK, body);
    }

    private static ResponseBody signupView() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/form.html").toPath());
        return new ResponseBody(HttpStatus.OK, body);
    }

    private static ResponseBody signup(HttpRequest httpRequest) throws IOException {
        String requestBody = httpRequest.getBody();
        Map<String, String> data = HttpRequestUtils.parseQueryString(requestBody);

        String userId = URLDecoder.decode(data.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(data.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(data.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(data.get("email"), StandardCharsets.UTF_8);
        User user = new User(userId, password, name, email);

        byte[] body = new byte[0];
        return new ResponseBody(HttpStatus.FOUND, body, "/");
    }

    private static ResponseBody defaultPath(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        return new ResponseBody(HttpStatus.OK, body);
    }
}
