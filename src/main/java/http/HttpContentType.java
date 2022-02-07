package http;

import java.util.HashMap;
import java.util.Map;

public class HttpContentType {
    private static final Map<String, String> contentMap = new HashMap<>();

    private HttpContentType() {
    }

    static {
        contentMap.put("css", "text/css");
        contentMap.put("html", "text/html; charset=utf-8");
        contentMap.put("js", "application/javascript");
    }

    public static String getContentType(String url) {
        String[] urlToken = url.split("\\.");
        return contentMap.get(urlToken[urlToken.length - 1]);
    }
}
