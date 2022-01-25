package util;

import model.User;
import network.HttpMethod;
import network.HttpRequest;
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
        Map<String, String> queryString = httpRequest.getQueryString();

        if (httpMethod.equals(HttpMethod.GET)) {
            switch (path) {
                case "/" : return indexView();
                case "/user/signup" : return signupView();
                case "/user/create" : return signup(queryString);
                default : return defaultPath(path);
            }
        }
        return null;
    }

    private static ResponseBody indexView() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        return new ResponseBody("200", body);
    }

    private static ResponseBody signupView() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/form.html").toPath());
        return new ResponseBody("200", body);
    }

    private static ResponseBody signup(Map<String, String> queryString) throws IOException {
        String userId = URLDecoder.decode(queryString.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(queryString.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(queryString.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(queryString.get("email"), StandardCharsets.UTF_8);
        User user = new User(userId, password, name, email);

        log.debug(user.toString());

        byte[] body = Files.readAllBytes(new File("./webapp/user/list.html").toPath());
        return new ResponseBody("200", body);
    }

    private static ResponseBody defaultPath(String path) throws IOException{
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        return new ResponseBody("200", body);
    }
}
