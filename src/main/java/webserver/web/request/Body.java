package webserver.web.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Body {

    private Map<String, String> body = new HashMap<>();

    public Body(List<String> request) {
        /*request.forEach(req -> {
            req = req.replaceFirst(":", "|");
            String[] split = req.split("\\|");
            body.put(split[0], split[1]);
        });*/
    }
}
