package util;

import java.util.Map;

public class HttpResponseUtils {

    public static String cookieString(Map<String, String> cookie) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : cookie.keySet()) {
            stringBuilder.append(key + "=" + cookie.get(key) + "; ");
        }
        return stringBuilder.toString();
    }

    public static String contentTypeFromPath(String responseDataPath) {
        String[] splitPath = responseDataPath.split("\\.");
        if (splitPath.length == 1) {
            return "text/html";
        }
        String extension = splitPath[splitPath.length-1];
        if (extension.equals("html")) {
            return "text/html";
        }
        if (extension.equals("css")) {
            return "text/css";
        }
        if (extension.equals("js")) {
            return "application/javascript";
        }
        if (extension.equals("ico")) {
            return "image/x-icon";
        }
        return "application/octet-stream";
    }
}
