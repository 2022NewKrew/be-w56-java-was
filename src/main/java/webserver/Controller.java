package webserver;

import model.User;
import service.UserService;
import util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    public static final Controller INSTANCE = new Controller();

    private final UserService userService;
    private final List<String> RESTRICTED_PAGES = Arrays.asList("/user/list.html");

    private Controller() {
        this.userService = UserService.INSTANCE;
    }

    public byte[] getRequest(Map<String, String> requestInfo) {
        byte[] body;
        try {
            String targetResource = requestInfo.get("target");
            boolean authorized = checkAuthorization(requestInfo);
            if (RESTRICTED_PAGES.contains(targetResource) && !authorized)
                targetResource = "/user/login.html";
            if (targetResource.equals("/user/list.html"))
                return getUserList();
            body = Files.readAllBytes(new File("./webapp" + targetResource).toPath());
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

    private boolean checkAuthorization(Map<String, String> requestInfo) {
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(requestInfo.getOrDefault("Cookie", ""));
        return Boolean.parseBoolean(cookieMap.getOrDefault("logined", "false"));
    }

    private byte[] getUserList() throws IOException {
        String[] tokens = (Files.readString(Path.of("./webapp/user/list.html"))).split("</?tbody>");
        StringBuilder sb = new StringBuilder(tokens[0] + "<tbody>");
        sb.append(userService.getUserList());
        sb.append("</tbody>").append(tokens[2]);
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
