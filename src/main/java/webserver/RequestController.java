package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestController {

    private static final Logger log = LoggerFactory.getLogger(RequestController.class);
    private static final DataBase database = new DataBase();

    public static Map<String, String> uriMatcher(String methodType, String uri, Map<String, String> requestBody) {
        if (methodType.equals("GET") && uri.equals("/user/create")) {
            postUserByGetRequest(uri);
        }
        else if (methodType.equals("POST") && uri.equals("/user/create")) {
            return postUserByPostRequest(uri, requestBody);
        }
        else if (uri.equals("/user/login")) {
            return loginPostRequest(requestBody);
        }
        return null;
    }

    private static void postUserByGetRequest(String uri) {
        String[] parsedUri = uri.split("\\?");
        String path = parsedUri[0];
        String queryString = parsedUri[1];
        Map<String, String> argsContainer = HttpRequestUtils.parseQueryString(queryString);
        User userEntity = new User(argsContainer.get("userId"), argsContainer.get("password"), argsContainer.get("name"), argsContainer.get("email"));
        database.addUser(userEntity);
        log.debug("USER CREATED: " + userEntity.toString());
    }

    private static Map<String, String> postUserByPostRequest(String uri, Map<String, String> requestBody) {
        User userEntity = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
        database.addUser(userEntity);
        log.debug("USER CREATED: " + userEntity.toString());
        Map<String, String> redirectUrl = new HashMap<>();
        redirectUrl.put("redirectUrl", "http://localhost:8080/index.html");
        return redirectUrl;
    }

    private static Map<String, String> loginPostRequest(Map<String, String> requestBody) {
        User userInfo = database.findUserById(requestBody.get("userId"));
        Map<String, String> result = new HashMap<>();
        if (userInfo != null && userInfo.getPassword().equals(requestBody.get("password"))) {
            result.put("redirectUrl", "http://localhost:8080/index.html");
            result.put("Set-Cookie", "logined=true; Path=/");
            log.debug("login success");
        }
        else {
            result.put("redirectUrl", "http://localhost:8080/user/login_failed.html");
            result.put("Set-Cookie", "logined=false; Path=/");
            log.debug("login failed");
        }
        return result;
    }

}
