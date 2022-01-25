package http.util;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseUtils {

    private static Map<String, String> mimeTypeMap;

    static {
        mimeTypeMap = new HashMap<>();
        mimeTypeMap.put(".htm", "text/html");
        mimeTypeMap.put(".html", "text/html");
        mimeTypeMap.put(".css", "text/css");
        mimeTypeMap.put(".js", "text/javascript");
        mimeTypeMap.put(".ttf", "font/ttf");
        mimeTypeMap.put(".woff", "font/woff");
    }

    public static String getContentsType(String extName) {
        return mimeTypeMap.get(extName);
    }
}
