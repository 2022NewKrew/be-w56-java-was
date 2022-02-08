package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class RequestMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static Map<String, String> requestMapping (Map<String, String> requestMap) {

        if (requestMap.get("method").equals("GET")) {
            return GetMapper.getMapping(requestMap);
        }

        if (requestMap.get("method").equals("POST")) {
            return PostMapper.postMapping(requestMap);
        }
        return null;
    }
}
