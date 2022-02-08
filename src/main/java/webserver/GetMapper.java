package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class GetMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static String getMapping (Map<String, String> requestMap) {

        // URL에서 접근경로와 이름=값 추출
        List<String> urlElement = List.of(requestMap.get("url").split("\\?"));
        String requestPath = urlElement.get(0);
        log.debug("urlPath : {}", requestPath);

        // 이름=값 추출
        Map<String, String> dataPairs = null;
        if (urlElement.size() > 1) {
            dataPairs = parseQueryString(urlElement.get(1));
        }

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
