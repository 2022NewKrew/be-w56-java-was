package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WebController {
    private static final Logger log = LoggerFactory.getLogger(WebController.class);
    private static final Map<String, RequestMapping> requestMap;

    static {
        requestMap = new HashMap<>();
        for(RequestMapping value : RequestMapping.values()) {
        }
    }
}
