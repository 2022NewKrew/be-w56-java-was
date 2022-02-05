package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MemberController {
    final private static String CREATE_USER = "/user/create";

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    public static void requestMapping(String requestUrl, Map<String, String> requestParam){
        log.info("Get Url: {}", requestUrl);
        if (requestUrl.equals(CREATE_USER)){
            User user = new User(requestParam);
            log.info("Create New User : {}", user);
            log.info("Url Changed: {}", requestUrl);
        }
    }
}
