package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class PostMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static Map<String, String> postMapping (Map<String, String> requestMap) {
        String requestPath = requestMap.get("url");

        Map<String, String> responseMap = new HashMap<>();

        // 회원가입
        if (requestPath.equals("/user/create")) {
            log.debug("postMapping -> user create");

            Map<String, String> userInfo;
            userInfo = parseQueryString(requestMap.get("body"));
            log.debug("userId : {}", userInfo.get("userId"));
            log.debug("password : {}", userInfo.get("password"));
            log.debug("name : {}", userInfo.get("name"));
            log.debug("email : {}", userInfo.get("email"));

            DataBase.addUser(new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email")));

            responseMap.put("templatePath", "/index.html");
            return responseMap;
        }

        // 로그인
        if (requestPath.equals("/user/login")) {
            log.debug("postMapping -> user login");

            Map<String, String> loginInfo;
            loginInfo = parseQueryString(requestMap.get("body"));
            if (UserService.loginValidation(loginInfo.get("userId"), loginInfo.get("password"))) {
                responseMap.put("templatePath", "/index.html");
                responseMap.put("cookie", "logined=true; Path=/");
                return responseMap;
            }
            responseMap.put("templatePath", "/user/login_failed.html");
            responseMap.put("cookie", "logined=false; Path=/");
            return responseMap;
        }

        requestMap.put("templatePath", requestPath);
        return requestMap;
    }
}
