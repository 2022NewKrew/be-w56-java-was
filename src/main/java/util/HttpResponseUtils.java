package util;

public class HttpResponseUtils {

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
