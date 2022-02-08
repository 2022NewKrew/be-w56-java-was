package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class PostMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static String postMapping (Map<String, String> requestMap) {
        String requestPath = requestMap.get("url");

        // 회원가입
        if (requestPath.equals("/user/create")) {
            log.debug("postMapping -> user create");

            Map<String, String> userInfo = new HashMap<>();
            userInfo = parseQueryString(requestMap.get("body"));
            log.debug("userId : {}", userInfo.get("userId"));
            log.debug("password : {}", userInfo.get("password"));
            log.debug("name : {}", userInfo.get("name"));
            log.debug("email : {}", userInfo.get("email"));

            DataBase.addUser(new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email")));

            return "/index.html";
        }
        return requestPath;
    }
}
