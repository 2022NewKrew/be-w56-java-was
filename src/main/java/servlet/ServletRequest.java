package servlet;

import http.header.Cookie;
import http.header.Cookies;
import http.startline.HttpMethod;
import servlet.container.MappingKey;

import java.util.HashMap;
import java.util.Map;

public class ServletRequest {
    private final HttpMethod method;
    private final String path;
    private final Map<String, String> parameters;
    private final Cookies cookies;

    public ServletRequest(HttpMethod method, String path, Map<String, String> queryParameters, Map<String, String> bodyParameters, Cookies cookies) {
        this.method = method;
        this.path = path;
        this.cookies = cookies;
        parameters = concatParameters(queryParameters, bodyParameters);
    }

    private Map<String, String> concatParameters(Map<String, String> queryParameters, Map<String, String> bodyParameters) {
        Map<String, String> concat = new HashMap<>();
        if (queryParameters != null) {
            concat.putAll(queryParameters);
        }
        if (bodyParameters != null) {
            concat.putAll(bodyParameters);
        }
        return concat;
    }

    public MappingKey createMappingKey() {
        return new MappingKey(method.toString() + path);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Cookie getCookie(String key) {
        return cookies.getCookie(key);
    }

    public String getPath() {
        return path;
    }
}
