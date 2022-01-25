package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class RequestMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static String requestMapping (String requestMethod, String requestUrl) {
        // URL에서 접근경로와 이름=값 추출
        List<String> urlElement = List.of(requestUrl.split("\\?"));
        String requestPath = urlElement.get(0);
        log.debug("urlPath : {}", requestPath);

        // 이름=값 추출
        Map<String, String> dataPairs = null;
        if (urlElement.size() > 1) {
            dataPairs = parseQueryString(urlElement.get(1));
            log.debug("userId : {}", dataPairs.get("userId"));
            log.debug("password : {}", dataPairs.get("password"));
            log.debug("name : {}", dataPairs.get("name"));
            log.debug("email : {}", dataPairs.get("email"));
        }

        if (requestMethod.equals("GET")) {
            return GetMapper.getMapping(requestPath, dataPairs);
        }
        return null;
    }
}
