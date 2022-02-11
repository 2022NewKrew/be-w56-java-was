package util;

public class HttpResponseUtils {

    public static String acceptType(String path) {
        if (path.endsWith(".css")) {
            return "text/css";
        }
        if (path.endsWith(".js")) {
            return "application/javascript";
        }
        if (path.endsWith(".ttf")) {
            return "application/x-font-ttf";
        }
        if (path.endsWith("woff")) {
            return "application/x-font-woff";
        }
        if (path.endsWith("woff2")) {
            return "application/x-font-woff";
        }
        if (path.endsWith(".ico")) {
            return "image/x-icon";
        }
        return "text/html";
    }
}
