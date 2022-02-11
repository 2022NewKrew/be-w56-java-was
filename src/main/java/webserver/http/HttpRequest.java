package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final Map<String, String> cookies;
    private final Map<String, String> parameters;
    private final String version;

    public HttpRequest(String method, String path, Map<String, String> headers, Map<String, String> parameters, String version) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.cookies = generateCookiesFromHeaders();
        this.parameters = parameters;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getCookies() { return cookies; }

    public boolean isLoggedIn() {
        try {
            return Boolean.parseBoolean(cookies.get("loggedin"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    private Map<String, String> generateCookiesFromHeaders() {
        try {
            String[] cookies = headers.get("Cookie").split("; ");
            Map<String, String> result = new HashMap<>();
            Arrays.stream(cookies).forEach(cookie -> {
                String key = cookie.split("=")[0];
                String value = cookie.split("=")[1];
                result.put(key, value);
            });
            return result;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
