package httpmodel;

import java.util.Map;

public class HttpRequestHeader {

    private static final String ACCEPT = "Accept";

    private final Map<String, String> header;

    public HttpRequestHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getParameter(String key) {
        return header.get(key);
    }

    public String acceptType(Uri uri) {
        if (header.containsKey(ACCEPT)) {
            String value = header.getOrDefault(ACCEPT, "");
            if (value.contains("text/css") || uri.getResourceUri().endsWith(".css")) {
                return "text/css";
            }
            if (value.contains("javascript") || uri.getResourceUri().endsWith(".js")) {
                return "application/javascript";
            }
            if (uri.getResourceUri().endsWith(".ttf")) {
                return "application/x-font-ttf";
            }
            if (uri.getResourceUri().contains(".woff")) {
                return "application/x-font-woff";
            }
            if (uri.getResourceUri().endsWith(".ico")) {
                return "image/x-icon";
            }
        }
        return "text/html";
    }
}
