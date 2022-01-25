package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GetMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static String getMapping (String requestPath, Map<String, String> dataPairs) {

        // 메인페이지
        if (requestPath.equals("/")) {
            log.debug("getMapping -> /");
            return "/index.html";
        }

        // 메인페이지
        if (requestPath.equals("/index")){
            log.debug("getMapping -> /index");
            return "/index.html";
        }

        // 회원가입
        if (requestPath.equals("/user/create")) {
            log.debug("getMapping -> user create");
            DataBase.addUser(new User(dataPairs.get("userId"), dataPairs.get("password"), dataPairs.get("name"), dataPairs.get("email")));
            return "/index.html";
        }

        return requestPath;
    }
}
