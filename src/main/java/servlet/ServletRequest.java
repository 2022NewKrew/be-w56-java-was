package servlet;

import http.header.Cookie;
import http.header.Cookies;
import http.startline.HttpMethod;
import servlet.container.MappingKey;

import java.util.Map;

public class ServletRequest {
    private HttpMethod method;
    private String path;
    private Map<String, String> parameters;
    private Cookies cookies;

    public ServletRequest(HttpMethod method, String path, Map<String, String> parameters, Cookies cookies) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.cookies = cookies;
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
