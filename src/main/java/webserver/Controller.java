package webserver;

import model.User;
import service.UserService;
import util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    public static final Controller INSTANCE = new Controller();

    private final UserService userService;

    private Controller() {
        this.userService = UserService.INSTANCE;
    }

    public byte[] getRequest(Map<String, String> requestInfo) {
        byte[] body;
        try {
            body = Files.readAllBytes(new File("./webapp" + requestInfo.get("target")).toPath());
        } catch (IOException e) {
            body = "Hello World".getBytes();
        }
        return body;
    }

    public Map<String, String> postRequest(Map<String, String> requestInfo) {
        String target = requestInfo.get("target");
        if (target.equals("/user/login")) {
            return login(requestInfo.get("body"));
        }
        if (target.equals("/user/create")) {
            createUser(requestInfo.get("body"));
            return Map.of("location", "/index.html");
        }
        return Map.of("location", "/");
    }

    private void createUser(String signUpInfo) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(signUpInfo);
        User newUser = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        userService.addUser(newUser);
    }

    private Map<String, String> login(String signUpInfo) {
        Map<String, String> userLoginInfo = HttpRequestUtils.parseQueryString(signUpInfo);
        Map<String, String> postHeaderInfo = new HashMap<>();
        boolean loginSuccess = userService.userLogin(userLoginInfo.get("userId"), userLoginInfo.get("password"));
        String redirectPath = loginSuccess ? "/index.html" : "/user/login_failed.html";
        postHeaderInfo.put("location", redirectPath);
        postHeaderInfo.put("cookie", "logined=" + loginSuccess + "; Path=/");

        return postHeaderInfo;
    }
}
